package com.hieutt.ecommerceweb.dto;

import com.hieutt.ecommerceweb.entity.Book;
import com.hieutt.ecommerceweb.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {
    private Long id;
    private int quantity;
    private Book book;
    private Order order;
}
