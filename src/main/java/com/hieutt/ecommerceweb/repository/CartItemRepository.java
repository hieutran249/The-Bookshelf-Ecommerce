package com.hieutt.ecommerceweb.repository;

import com.hieutt.ecommerceweb.entity.Book;
import com.hieutt.ecommerceweb.entity.CartItem;
import com.hieutt.ecommerceweb.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByShoppingCartAndBook(ShoppingCart cart, Book book);
    boolean existsByShoppingCartAndBook(ShoppingCart cart, Book book);
}
