package com.hieutt.ecommerceweb.service;

import com.hieutt.ecommerceweb.dto.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    List<OrderDto> getAllOrders();
    OrderDto getOrderById(Long id);
    OrderDto updateOrder(Long id, OrderDto orderDto);
    void deleteOrder(Long id);
}
