package com.hieutt.ecommerceweb.service;

import com.hieutt.ecommerceweb.dto.OrderDto;
import com.hieutt.ecommerceweb.entity.User;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(User user, String paymentMethod);
    List<OrderDto> getAllOrders();
    OrderDto getOrderById(Long id);
    OrderDto updateOrder(Long id, OrderDto orderDto);
    void deleteOrder(Long id);
    List<OrderDto> getOrdersByUser(User user);
    void cancelOrder(Long orderId);
    void acceptOrder(Long orderId);
    void rejectOrder(Long orderId);
    void doNextStage(Long orderId, String status);
    void returnOrder(Long orderId);
}
