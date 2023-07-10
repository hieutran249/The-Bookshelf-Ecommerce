package com.hieutt.ecommerceweb.service;

import com.hieutt.ecommerceweb.dto.ShoppingCartDto;
import com.hieutt.ecommerceweb.entity.ShoppingCart;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface ShoppingCartService {
    ShoppingCartDto createShoppingCart(ShoppingCartDto cartDto);
    List<ShoppingCartDto> getAllShoppingCarts();
    ShoppingCartDto getShoppingCartById(Long id);
    ShoppingCartDto updateShoppingCart(Long id, ShoppingCartDto cartDto);
    void deleteShoppingCart(Long id);
    Map<String, String> addToCart(Long bookId, Principal principal);
    ShoppingCart getCartByUser(Principal principal);
    Map<String, String> updateCart(Long cartItemId, int quantity, String action);
}
