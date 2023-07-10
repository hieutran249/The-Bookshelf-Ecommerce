package com.hieutt.ecommerceweb.dto;

import com.hieutt.ecommerceweb.entity.CartItem;
import com.hieutt.ecommerceweb.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingCartDto {
    private Long id;
    private int quantity;
    private double totalPrice;
    private User user;
    private List<CartItem> cartItemList;
}
