package com.hieutt.ecommerceweb.dto;

import com.hieutt.ecommerceweb.entity.Role;
import com.hieutt.ecommerceweb.entity.ShoppingCart;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    @NotEmpty(message = "Please provide your first name 🤓")
    private String firstName;
    @NotEmpty(message = "Please provide your last name 🤓")
    private String lastName;
    @NotEmpty(message = "Please provide your email 🤓")
    @Email(message = "This is not an email 🤓 Please try again 🙏")
    private String email;
    private String address;
    private String phoneNumber;
    private String image;
}
