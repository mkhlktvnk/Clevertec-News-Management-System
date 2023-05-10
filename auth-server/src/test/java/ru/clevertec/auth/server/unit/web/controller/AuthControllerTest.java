package ru.clevertec.auth.server.unit.web.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.clevertec.auth.server.web.controller.AuthController;
import ru.clevertec.auth.server.web.model.AuthRequest;
import ru.clevertec.auth.server.web.model.AuthResponse;
import ru.clevertec.auth.server.service.AuthService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void authenticateShouldReturnTokenWithOkStatus() {
        String username = "user123";
        String token = "test-token";
        AuthRequest authRequest = AuthRequest.builder()
                .username(username)
                .password("password123")
                .build();
        doReturn(token).when(authService).authenticate(authRequest);

        ResponseEntity<String> response = authController.authenticate(authRequest);

        verify(authService).authenticate(authRequest);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(token);
    }

    @Test
    void registerShouldReturnAuthResponseWithOkStatus() {
        String username = "user123";
        AuthRequest authRequest = AuthRequest.builder()
                .username(username)
                .password("password123")
                .build();
        AuthResponse authResponse = AuthResponse.builder()
                .id(1L)
                .username(username)
                .build();
        doReturn(authResponse).when(authService).register(authRequest);

        ResponseEntity<AuthResponse> response = authController.register(authRequest);

        verify(authService).register(authRequest);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo(authRequest.getUsername());
    }

    @Test
    void validateShouldReturnOkStatus() {
        String token = "Bearer test-token";
        doReturn(true).when(authService).isTokenValid(token);

        ResponseEntity<?> response = authController.validate(token);

        verify(authService).isTokenValid(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void validateTokenShouldReturnUnauthorizedStatus() {
        String token = "Bearer test-token";
        doReturn(false).when(authService).isTokenValid(token);

        ResponseEntity<?> response = authController.validate(token);

        verify(authService).isTokenValid(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}