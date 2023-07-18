package com.hieutt.ecommerceweb.service.impl;

import com.hieutt.ecommerceweb.dto.OrderDto;
import com.hieutt.ecommerceweb.entity.*;
import com.hieutt.ecommerceweb.exception.ResourceNotFoundException;
import com.hieutt.ecommerceweb.repository.CartItemRepository;
import com.hieutt.ecommerceweb.repository.OrderItemRepository;
import com.hieutt.ecommerceweb.repository.OrderRepository;
import com.hieutt.ecommerceweb.repository.ShoppingCartRepository;
import com.hieutt.ecommerceweb.service.EmailSenderService;
import com.hieutt.ecommerceweb.service.OrderService;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final EmailSenderService senderService;
    private final ModelMapper modelMapper = new ModelMapper();

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ShoppingCartRepository shoppingCartRepository, CartItemRepository cartItemRepository, EmailSenderService senderService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartItemRepository = cartItemRepository;
        this.senderService = senderService;
    }

    @Override
    public OrderDto createOrder(User user, String paymentMethod) {
        ShoppingCart cart = shoppingCartRepository.findShoppingCartByUser(user);
        double totalPriceOrder = cart.getTotalPrice() > 1000000 ? cart.getTotalPrice() : cart.getTotalPrice() + 30000;

        Order order = Order.builder()
                .orderStatus(OrderStatus.QUEUED)
                .createdAt(LocalDateTime.now())
                .totalPrice(totalPriceOrder)
                .paymentMethod(paymentMethod)
                .accepted(false)
                .quantity(cart.getQuantity())
                .user(user)
                .build();
        if (paymentMethod.equals("Bank card")) {
            order.setPaidAt(LocalDateTime.now());
        }
        orderRepository.save(order);

        List<CartItem> cartItems = cart.getCartItemList();
        cartItems.forEach(cartItem -> {
                    OrderItem orderItem = OrderItem.builder()
                            .quantity(cartItem.getQuantity())
                            .totalPrice(cartItem.getTotalPrice())
                            .book(cartItem.getBook())
                            .order(order)
                            .build();
                    orderItemRepository.save(orderItem);
                }
        );
        // reset shopping cart after place order
        cartItemRepository.deleteAll(cartItems);
        cartItems.clear();
        cart.setQuantity(0);
        cart.setTotalPrice(0);
        shoppingCartRepository.save(cart);

        // send mail to customer
        sendMail(user.getEmail(),
                "[The Bookshelf] PLACED ORDER SUCCESSFULLY",
                "Dear " + user.getFirstName() + user.getLastName() + ",\n" + "\n"
                + "You have successfully placed an order from The Bookshelf 📚" + "\n"
                + "Please patiently wait for us to accept your order 🙏" + "\n" + "\n"
                + "From admin."
        );

        return mapToDto(order);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        checkRefundExpiration(orders);
        return orders.stream()
                .map(order -> mapToDto(order))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = findOrderById(id);
        return mapToDto(order);
    }

    @Override
    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        Order order = findOrderById(id);
        order.setUpdatedAt(LocalDateTime.now());
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setDeliveredAt(orderDto.getDeliveredAt());
        order.setPaidAt(orderDto.getPaidAt());
        order.setPaymentMethod(orderDto.getPaymentMethod());
        order.setAccepted(orderDto.isAccepted());
        order.setQuantity(orderDto.getQuantity());

        orderRepository.save(order);
        return mapToDto(order);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = findOrderById(id);
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrdersByUser(User user) {
        List<Order> orders = orderRepository.findOrdersByUser(user);
        checkRefundExpiration(orders);
        return orders.stream()
                .map(order -> mapToDto(order))
                .collect(Collectors.toList());
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = findOrderById(orderId);
        order.setOrderStatus(OrderStatus.CANCELED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        // send mail to customer
        sendMail(order.getUser().getEmail(),
                "[The Bookshelf] CANCELED ORDER SUCCESSFULLY",
                "Dear " + order.getUser().getFirstName() + order.getUser().getLastName() + ",\n" + "\n"
                        + "You have successfully canceled an order from The Bookshelf 📚" + "\n"
                        + "Please let us know if you have any problems 🙇‍♂️" + "\n" + "\n"
                        + "From admin."
        );
    }

    @Override
    public void acceptOrder(Long orderId) {
        Order order = findOrderById(orderId);
        order.setAccepted(true);
        order.setOrderStatus(OrderStatus.PACKAGING);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        // send mail to customer
        sendMail(order.getUser().getEmail(),
                "[The Bookshelf] YOUR ORDER IS ACCEPTED",
                "Dear " + order.getUser().getFirstName() + order.getUser().getLastName() + ",\n" + "\n"
                        + "An order of you from The Bookshelf has been accepted 🎉" + "\n"
                        + "The books are being packaged 📦 and soon be delivered 🚚" + "\n" + "\n"
                        + "From admin."
        );
    }

    @Override
    public void rejectOrder(Long orderId) {
        Order order = findOrderById(orderId);
        order.setOrderStatus(OrderStatus.REJECTED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        // send mail to customer
        sendMail(order.getUser().getEmail(),
                "[The Bookshelf] YOUR ORDER IS REJECTED",
                "Dear " + order.getUser().getFirstName() + order.getUser().getLastName() + ",\n" + "\n"
                        + "An order of you from The Bookshelf has been rejected because ... 🙅‍♂️" + "\n"
                        + "We're sorry about your order 😢" + "\n" + "\n"
                        + "From admin."
        );
    }

    @Override
    public void doNextStage(Long orderId, String status) {
        Order order = findOrderById(orderId);
        if (status.equals("delivering")) {
            order.setOrderStatus(OrderStatus.DELIVERING);
            order.setUpdatedAt(LocalDateTime.now());

            // send mail to customer
            sendMail(order.getUser().getEmail(),
                    "[The Bookshelf] ORDER IS BEING DELIVERED",
                    "Dear " + order.getUser().getFirstName() + order.getUser().getLastName() + ",\n" + "\n"
                            + "An order of you from The Bookshelf 📚 is being delivered 🚚" + "\n"
                            + "Your books will soon come to your place 🏡" + "\n" + "\n"
                            + "From admin."
            );
        }
        if (status.equals("delivered")) {
            order.setOrderStatus(OrderStatus.DELIVERED);
            order.setDeliveredAt(LocalDateTime.now());
            if (order.getPaymentMethod().equals("Cash")) {
                order.setPaidAt(LocalDateTime.now());
            }
            order.setUpdatedAt(LocalDateTime.now());

            // send mail to customer
            sendMail(order.getUser().getEmail(),
                    "[The Bookshelf] ORDER HAS BEEN DELIVERED",
                    "Dear " + order.getUser().getFirstName() + order.getUser().getLastName() + ",\n" + "\n"
                            + "An order of you from The Bookshelf 📚 has been delivered to your place" + "\n"
                            + "Take and enjoy 🕺" + "\n" + "\n"
                            + "From admin."
            );
        }
        orderRepository.save(order);
    }

    @Override
    public void returnOrder(Long orderId) {
        Order order = findOrderById(orderId);
        order.setOrderStatus(OrderStatus.RETURNED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        // send mail to customer
        sendMail(order.getUser().getEmail(),
                "[The Bookshelf] RETURNED ORDER SUCCESSFULLY",
                "Dear " + order.getUser().getFirstName() + order.getUser().getLastName() + ",\n" + "\n"
                        + "You have successfully returned an order from The Bookshelf 📚" + "\n"
                        + "You will be soon refunded 💰" + "\n" + "\n"
                        + "From admin."
        );
    }

    private void checkRefundExpiration(List<Order> orders) {
        orders.forEach(order -> {
            if (order.getDeliveredAt().plusDays(7).isBefore(LocalDateTime.now()) && order.getOrderStatus().name().equals("DELIVERED")) {
                order.setOrderStatus(OrderStatus.CLOSED);
            }
        });
    }

    private void sendMail(String receiver, String subject, String body) {
        senderService.sendEmail(receiver, subject, body);
    }

    private Order findOrderById(Long id) {
        return orderRepository
                .findById(id).orElseThrow(()
                        -> new ResourceNotFoundException("Order", "id", id));
    }

    private OrderDto mapToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
    private Order mapToEntity(OrderDto orderDto) {
        return modelMapper.map(orderDto, Order.class);
    }
}
