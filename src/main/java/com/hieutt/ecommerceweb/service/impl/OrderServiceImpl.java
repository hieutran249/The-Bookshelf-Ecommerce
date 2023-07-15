package com.hieutt.ecommerceweb.service.impl;

import com.hieutt.ecommerceweb.dto.OrderDto;
import com.hieutt.ecommerceweb.entity.*;
import com.hieutt.ecommerceweb.exception.ResourceNotFoundException;
import com.hieutt.ecommerceweb.repository.CartItemRepository;
import com.hieutt.ecommerceweb.repository.OrderItemRepository;
import com.hieutt.ecommerceweb.repository.OrderRepository;
import com.hieutt.ecommerceweb.repository.ShoppingCartRepository;
import com.hieutt.ecommerceweb.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ShoppingCartRepository shoppingCartRepository, CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartItemRepository = cartItemRepository;
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

        return mapToDto(order);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
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
