package com.hieutt.ecommerceweb.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    private Long id;

    @NotEmpty(message = "Please provide the category name 🤓")
    @Size(min = 2, message = "The category name should have at least 2 characters 🤓")
    private String name;

    private boolean deleted;
}
