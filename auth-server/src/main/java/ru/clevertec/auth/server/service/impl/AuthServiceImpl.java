package ru.clevertec.auth.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.clevertec.auth.server.entity.User;
import ru.clevertec.auth.server.web.mapper.UserMapper;
import ru.clevertec.auth.server.web.model.AuthRequest;
import ru.clevertec.auth.server.web.model.AuthResponse;
import ru.clevertec.auth.server.service.AuthService;
import ru.clevertec.auth.server.service.TokenService;
import ru.clevertec.auth.server.service.UserService;

/**
 * Implementation of {@link AuthService} interface.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    /**
     * Authenticates a user with the given username and password, and returns a JWT token.
     *
     * @param request The authentication request.
     * @return A JWT token.
     */
    @Override
    public String authenticate(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        return tokenService.generateToken(authentication.getName(), authentication.getAuthorities());
    }

    /**
     * Registers a new user with the given authentication request.
     *
     * @param authRequest The authentication request.
     * @return An authentication response containing the newly registered user's details.
     */
    @Override
    public AuthResponse register(AuthRequest authRequest) {
        User user = mapper.mapToEntity(authRequest);

        return mapper.mapToModel(userService.saveUser(user));
    }

    /**
     * Checks whether the given token is valid.
     *
     * @param token The token to check.
     * @return True if the token is valid, false otherwise.
     */
    @Override
    public Boolean isTokenValid(String token) {
        return tokenService.isTokenValid(token);
    }
}

