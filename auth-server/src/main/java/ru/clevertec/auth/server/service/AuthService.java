package ru.clevertec.auth.server.service;

import ru.clevertec.auth.server.web.model.AuthRequest;
import ru.clevertec.auth.server.web.model.AuthResponse;

public interface AuthService {
    String authenticate(AuthRequest request);

    AuthResponse register(AuthRequest request);

    Boolean isTokenValid(String token);
}
