package ru.clevertec.newsresource.service;

import org.springframework.security.core.userdetails.User;

/**
 * This interface defines methods for working with JWT tokens.
 */
public interface TokenService {

    /**
     * Retrieves user information from a JWT token.
     *
     * @param token the JWT token to retrieve user information from
     * @return the user information decoded from the JWT token
     */
    User getUserInfoFromToken(String token);
}

