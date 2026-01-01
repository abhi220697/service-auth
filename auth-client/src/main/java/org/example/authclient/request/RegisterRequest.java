package org.example.authclient.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Schema(description = "User registration request")
public record RegisterRequest(
        @Schema(description = "Username", example = "john_doe")
        @NotBlank @Size(min = 3, max = 20) String username,

        @Schema(description = "Email address", example = "john@example.com")
        @NotBlank @Email String email,

        @Schema(description = "Password", example = "mypassword123")
        @NotBlank @Size(min = 8) String password,

        @Schema(description = "First name", example = "John")
        @NotBlank String firstName,

        @Schema(description = "Last name", example = "Doe")
        @NotBlank String lastName,

        @Schema(description = "Phone number", example = "+1234567890")
        String phone,

        @Schema(description = "roles",example = "")
        Set<String> roles
) {}

