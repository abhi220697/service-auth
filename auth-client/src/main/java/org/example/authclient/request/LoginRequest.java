package org.example.authclient.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "User login credentials")
public record LoginRequest(
        @Schema(description = "Username or email", example = "john_doe")
        @NotBlank String username,

        @Schema(description = "Password", example = "mypassword123")
        @NotBlank String password
) {}
