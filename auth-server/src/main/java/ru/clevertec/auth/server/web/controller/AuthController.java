package ru.clevertec.auth.server.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.auth.server.web.model.AuthRequest;
import ru.clevertec.auth.server.web.model.AuthResponse;
import ru.clevertec.auth.server.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/token")
    public ResponseEntity<String> authenticate(@RequestBody @Valid AuthRequest authRequest) {
        String token = authService.authenticate(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthRequest authRequest) {
        AuthResponse response = authService.register(authRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/auth/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String token) {
        return authService.isTokenValid(token) ? ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
