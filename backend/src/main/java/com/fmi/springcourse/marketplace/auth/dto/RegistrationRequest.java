package com.fmi.springcourse.marketplace.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotBlank(message = "Profile name is required")
        @Size(min = 3, max = 20, message = "Profile name must be between 3 and 20 characters")
        String profileName,

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        String email,

        @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z]).*$",
            message = "Password must contain at least one letter and one number"
        )
        @Size(min = 8, message = "Password must be at least 8 characters")
        @NotBlank(message = "Password is required") // TO BE REMOVED (Added for convenience)
        String password) {
}
