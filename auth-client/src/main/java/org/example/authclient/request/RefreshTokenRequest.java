package org.example.authclient.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request to refresh expired access token using refresh token")
public record RefreshTokenRequest(
        @Schema(
                description = "Valid refresh token obtained during login/register",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiaWF0IjoxNzM1MTI5NTgwLCJleHAiOjE3MzUxMzY5ODB9.signature"
        )
        @NotBlank(message = "Refresh token is required")
        String refreshToken
) {}
