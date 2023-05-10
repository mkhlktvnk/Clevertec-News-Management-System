package ru.clevertec.auth.server.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.auth.server.service.AuthService;
import ru.clevertec.auth.server.web.model.AuthRequest;
import ru.clevertec.auth.server.web.model.AuthResponse;

/**
 * Controller class for authentication-related endpoints.
 */
@Tag(
        name = "Authentication API",
        description = "Operations for authentication / registration / validation user data"
)
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * Authenticates a user and returns an access token.
     *
     * @param authRequest the authentication request containing the user's credentials
     * @return a response entity containing the access token
     */
    @Operation(summary = "Authenticate user and return access token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User was authenticated and token was returned"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User was not authenticated"
            )
    })
    @PostMapping("/auth/token")
    public ResponseEntity<String> authenticate(@RequestBody @Valid AuthRequest authRequest) {
        String token = authService.authenticate(authRequest);
        return ResponseEntity.ok(token);
    }

    /**
     * Registers a new user.
     *
     * @param authRequest the registration request containing the user's credentials
     * @return a response entity containing the authentication response
     */
    @Operation(summary = "Register user")
    @ApiResponse(
            responseCode = "200",
            description = "User was registered"
    )
    @PostMapping("/auth/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthRequest authRequest) {
        AuthResponse response = authService.register(authRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Validates an access token.
     *
     * @param token the access token to validate
     * @return a response entity indicating whether the token is valid or not
     */
    @Operation(summary = "Validate access token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token is valid"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token is invalid"
            )
    })
    @GetMapping("/auth/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String token) {
        return authService.isTokenValid(token) ? ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}

