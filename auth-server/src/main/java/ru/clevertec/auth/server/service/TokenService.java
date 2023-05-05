package ru.clevertec.auth.server.service;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface TokenService {
    String generateToken(String username, Collection<? extends GrantedAuthority> roles);

    boolean isTokenValid(String token);
}
