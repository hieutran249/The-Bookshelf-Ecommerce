package com.hieutt.ecommerceweb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "books",
        uniqueConstraints = @UniqueConstraint(columnNames = ("title"))
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "author")
    private String author;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "published_year", nullable = false)
    private String publishedYear;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Lob
    @Column(
            name = "image",
            columnDefinition = "MEDIUMBLOB"
    )
    private String image;

    private boolean deleted;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "category_id",
            referencedColumnName = "id"
    )
    private Category category;

}
