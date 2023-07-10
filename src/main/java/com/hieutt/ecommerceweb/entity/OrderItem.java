package com.hieutt.ecommerceweb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private int quantity;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "book_id",
            referencedColumnName = "id"
    )
    private Book book;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "order_id",
            referencedColumnName = "id"
    )
    private Order order;
}
