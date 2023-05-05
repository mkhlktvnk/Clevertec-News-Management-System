package ru.clevertec.auth.server.service;

import ru.clevertec.auth.server.model.AuthRequest;
import ru.clevertec.auth.server.model.AuthResponse;

public interface AuthService {
    String authenticate(AuthRequest request);

    AuthResponse register(AuthRequest request);

    Boolean isTokenValid(String token);
}
