package org.example.authclient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.authclient.request.LoginRequest;
import org.example.authclient.request.RefreshTokenRequest;
import org.example.authclient.request.RegisterRequest;
import org.example.authclient.request.ValidateTokenRequest;
import org.example.authclient.response.AuthResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(
        name = "Authentication API",
        description = "User authentication, registration, and token management endpoints"
)
@RequestMapping(AuthApi.baseUrl)
public interface AuthApi {

    String baseUrl = "api/v1/auth";

    @Operation(
            summary = "Register new user",
            description = "Creates a new user account with authentication credentials and profile in User Service. Returns JWT access and refresh tokens."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request data (username/email exists, validation failed)"),
            @ApiResponse(responseCode = "409", description = "Username or email already exists"),
            @ApiResponse(responseCode = "500", description = "User Service unavailable")
    })
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AuthResponse> register(
            @Parameter(description = "User registration details")
            @Valid @RequestBody RegisterRequest request
    );

    @Operation(
            summary = "User login",
            description = "Authenticates user credentials and returns JWT access and refresh tokens"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Invalid username or password"),
            @ApiResponse(responseCode = "400", description = "Missing credentials")
    })
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AuthResponse> login(
            @Parameter(description = "Login credentials")
            @Valid @RequestBody LoginRequest request
    );

    @Operation(
            summary = "Refresh access token",
            description = "Exchanges valid refresh token for new access token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Token refreshed successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token"),
            @ApiResponse(responseCode = "400", description = "Missing refresh token")
    })
    @PostMapping(value = "/refresh", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AuthResponse> refreshToken(
            @Parameter(description = "Refresh token request")
            @Valid @RequestBody RefreshTokenRequest request
    );

    @Operation(
            summary = "Validate JWT token",
            description = "Validates access token and returns user information from token claims"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Token is valid",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Invalid or expired token"),
            @ApiResponse(responseCode = "400", description = "Missing token")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(value = "/validate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AuthResponse> validateToken(
            @Parameter(description = "Token to validate")
            @Valid @RequestBody ValidateTokenRequest request
    );

    @Operation(
            summary = "Logout user",
            description = "Invalidates refresh token and adds access token to blacklist"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Logged out successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid token")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    ResponseEntity<Void> logout();

    @Operation(
            summary = "Get current user info",
            description = "Returns authenticated user information from JWT token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User information",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    ResponseEntity<AuthResponse> getCurrentUser();

    @Operation(
            summary = "Forgot password",
            description = "Initiates password reset flow by sending reset token to user email"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reset token sent to email"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "429", description = "Too many requests - rate limited")
    })
    @PostMapping("/forgot-password")
    ResponseEntity<Void> forgotPassword(
            @Parameter(description = "User email")
            @RequestParam String email
    );

    @Operation(
            summary = "Reset password",
            description = "Resets user password using reset token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password reset successful"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired reset token"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/reset-password")
    ResponseEntity<Void> resetPassword(
            @Parameter(description = "Reset token")
            @RequestParam String token,
            @Parameter(description = "New password")
            @RequestParam String newPassword
    );
}

