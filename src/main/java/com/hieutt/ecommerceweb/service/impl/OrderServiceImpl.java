package com.hieutt.ecommerceweb.service.impl;

import com.hieutt.ecommerceweb.dto.OrderDto;
import com.hieutt.ecommerceweb.entity.Order;
import com.hieutt.ecommerceweb.entity.OrderStatus;
import com.hieutt.ecommerceweb.exception.ResourceNotFoundException;
import com.hieutt.ecommerceweb.repository.OrderRepository;
import com.hieutt.ecommerceweb.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = mapToEntity(orderDto);
        order.setOrderStatus(OrderStatus.QUEUED);
        order.setCreatedAt(LocalDateTime.now());
        order.setAccepted(false);

        Order newOrder = orderRepository.save(order);
        return mapToDto(newOrder);
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
