package com.hieutt.ecommerceweb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDto {
    @NotEmpty(message = "Please provide your first name!")
    private String firstName;

    @NotEmpty(message = "Please provide your last name!")
    private String lastName;

    @Email(message = "Please provide your email!")
    private String email;

    @NotEmpty(message = "Please provide your password!")
    @Size(min = 4, message = "Your password should have at least 4 characters!")
    private String password;

    @NotEmpty(message = "Please confirm your password!")
    private String confirmPassword;
}
