package com.hieutt.ecommerceweb.dto;

import com.hieutt.ecommerceweb.entity.CartItem;
import com.hieutt.ecommerceweb.entity.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {
    private Long id;

    @NotEmpty(message = "Please provide book title 🤓")
    @Size(min = 2, message = "A book title should have at least 2 characters 🤓")
    private String title;

    @NotEmpty(message = "Please provide book description 🤓")
    @Size(min = 10, message = "A book description should have at least 10 characters 🤓")
    private String description;

    private String author;

    private double price;

    @NotEmpty(message = "Please provide book published year 🤓")
    private String publishedYear;

    @Min(value = 1, message = "Book quantity should be at least 1 🤓")
    private int quantity;

    private String image;

    private boolean deleted;

    private Category category;
}
