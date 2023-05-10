package ru.clevertec.auth.server.service;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * A service for generating and validating JWT tokens.
 */
public interface TokenService {

    /**
     * Generates a JWT token for the given user and roles.
     *
     * @param username the username of the user for whom the token is being generated
     * @param roles    the roles of the user for whom the token is being generated
     * @return the generated JWT token
     */
    String generateToken(String username, Collection<? extends GrantedAuthority> roles);

    /**
     * Checks whether the given JWT token is valid.
     *
     * @param token the JWT token to validate
     * @return {@code true} if the token is valid, {@code false} otherwise
     */
    boolean isTokenValid(String token);
}

