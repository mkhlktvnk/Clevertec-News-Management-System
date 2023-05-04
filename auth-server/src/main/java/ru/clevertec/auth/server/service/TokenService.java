package ru.clevertec.auth.server.service;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenService {
    String generateToken(String username);

    boolean isTokenValid(String token);

    String getTokenFromRequest(HttpServletRequest request);

    String getLoginFromToken(String token);
}
