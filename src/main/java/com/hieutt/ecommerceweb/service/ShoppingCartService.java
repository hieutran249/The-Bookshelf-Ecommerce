package com.hieutt.ecommerceweb.service;

import com.hieutt.ecommerceweb.dto.OrderDto;
import com.hieutt.ecommerceweb.dto.ShoppingCartDto;

import java.util.List;

public interface ShoppingCartService {
    ShoppingCartDto createShoppingCart(ShoppingCartDto cartDto);
    List<ShoppingCartDto> getAllShoppingCarts();
    ShoppingCartDto getShoppingCartById(Long id);
    ShoppingCartDto updateShoppingCart(Long id, ShoppingCartDto cartDto);
    void deleteShoppingCart(Long id);
}
