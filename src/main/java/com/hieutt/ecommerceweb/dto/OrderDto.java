package com.hieutt.ecommerceweb.dto;

import com.hieutt.ecommerceweb.entity.User;
import com.hieutt.ecommerceweb.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private double totalPrice;
    private LocalDateTime deliveredAt;
    private LocalDateTime paidAt;
    private String paymentMethod;
    private boolean accepted;
    private int quantity;
    private User customer;
}
