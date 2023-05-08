package ru.clevertec.auth.server.integration.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.auth.server.integration.BaseIntegrationTest;
import ru.clevertec.auth.server.model.AuthRequest;
import ru.clevertec.auth.server.model.AuthResponse;
import ru.clevertec.auth.server.service.AuthService;

import static org.assertj.core.api.Assertions.assertThat;

class AuthServiceImplTest extends BaseIntegrationTest {

    @Autowired
    private AuthService authService;

    @Test
    void registerShouldRegisterUser() {
        AuthRequest request = AuthRequest.builder()
                .username("user123")
                .password("password123")
                .build();

        AuthResponse response = authService.register(request);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getUsername()).isEqualTo(request.getUsername());
    }

}