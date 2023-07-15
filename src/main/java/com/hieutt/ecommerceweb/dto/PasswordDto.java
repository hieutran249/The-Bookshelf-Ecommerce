package com.hieutt.ecommerceweb.dto;

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
public class PasswordDto {
    @NotEmpty(message = "Please provide your current password 🤓")
    private String oldPassword;

    @NotEmpty(message = "Please provide your new password 🤓")
    @Size(min = 4, message = "Password should have at least 4 characters 🤓")
    private String newPassword;

    @NotEmpty(message = "Please confirm your new password 🤓")
    private String confirmPassword;
}
