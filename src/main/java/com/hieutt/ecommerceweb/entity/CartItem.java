package com.hieutt.ecommerceweb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private int quantity;
    private double totalPrice;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "cart_id",
            referencedColumnName = "id"
    )
    private ShoppingCart shoppingCart;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "book_id",
            referencedColumnName = "id"
    )
    private Book book;


}
