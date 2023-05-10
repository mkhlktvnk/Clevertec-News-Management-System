package ru.clevertec.auth.server.service;

import ru.clevertec.auth.server.web.model.AuthRequest;
import ru.clevertec.auth.server.web.model.AuthResponse;

/**
 * Service interface for user authentication and authorization.
 */
public interface AuthService {

    /**
     * Authenticates a user based on the provided authentication request.
     *
     * @param request The authentication request.
     * @return A JWT token if the authentication was successful.
     */
    String authenticate(AuthRequest request);

    /**
     * Registers a new user based on the provided authentication request.
     *
     * @param request The authentication request.
     * @return The registered user details.
     */
    AuthResponse register(AuthRequest request);

    /**
     * Checks if the provided token is valid.
     *
     * @param token The token to check.
     * @return True if the token is valid, false otherwise.
     */
    Boolean isTokenValid(String token);
}

