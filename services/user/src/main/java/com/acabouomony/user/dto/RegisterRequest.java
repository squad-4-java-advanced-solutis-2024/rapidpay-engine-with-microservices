package com.acabouomony.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "RegisterRequest")
public record RegisterRequest(

        @NotNull(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        @Column(unique = true)
        String username,

        @NotNull(message = "Email is required")
        @Email(message = "Email is invalid")
        @Size(min = 3, max = 100, message = "Email must be between 3 and 50 characters")
        @Column(unique = true)
        String email,

        @NotNull(message = "Password is required")
        String password
) {
}
