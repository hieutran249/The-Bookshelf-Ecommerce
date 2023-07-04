package com.hieutt.ecommerceweb.dto;

import com.hieutt.ecommerceweb.entity.Book;
import com.hieutt.ecommerceweb.entity.ShoppingCart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {
    private Long id;
    private int quantity;
    private ShoppingCart shoppingCart;
    private Book book;
}
