package com.example.firebase.demo.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotNull
        @Email
        String email,

        @Size(min=8, max = 32, message = "Password must be at least 6 and at most 32 characters long.")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*(),.?\":{}|<>]).{2,}$", message = "A password must include at least one number and one special character.")
        @NotNull
        String password
) {
}