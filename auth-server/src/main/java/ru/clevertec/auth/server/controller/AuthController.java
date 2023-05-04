package ru.clevertec.auth.server.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.auth.server.model.AuthRequest;
import ru.clevertec.auth.server.model.AuthResponse;
import ru.clevertec.auth.server.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<String> authenticate(@RequestBody @Valid AuthRequest authRequest) {
        String token = authService.authenticate(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthRequest authRequest) {
        AuthResponse response = authService.register(authRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<AuthResponse> validate(HttpServletRequest request) {
        AuthResponse response = authService.validate(request);
        return ResponseEntity.ok(response);
    }

}
