package org.example.authclient.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request to validate JWT access token")
public record ValidateTokenRequest(
        @Schema(
                description = "JWT access token to validate",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE3MzUxMjk1ODAsImV4cCI6MTczNTEzNjk4MH0.signature"
        )
        @NotBlank(message = "Access token is required")
        String accessToken
) {}

