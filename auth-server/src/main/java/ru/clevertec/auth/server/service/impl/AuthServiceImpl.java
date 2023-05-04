package ru.clevertec.auth.server.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.clevertec.auth.server.entity.User;
import ru.clevertec.auth.server.mapper.UserMapper;
import ru.clevertec.auth.server.model.AuthRequest;
import ru.clevertec.auth.server.model.AuthResponse;
import ru.clevertec.auth.server.service.AuthService;
import ru.clevertec.auth.server.service.TokenService;
import ru.clevertec.auth.server.service.UserService;
import ru.clevertec.exception.handling.starter.exception.AuthenticationException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Override
    public String authenticate(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));

        return tokenService.generateToken(request.getUsername());
    }

    @Override
    public AuthResponse register(AuthRequest authRequest) {
        User user = mapper.mapToEntity(authRequest);

        return mapper.mapToModel(userService.saveUser(user));
    }

    @Override
    public AuthResponse validate(HttpServletRequest request) {
        String jwt = tokenService.getTokenFromRequest(request);
        if (!tokenService.isTokenValid(jwt)) {
            throw new AuthenticationException("");
        }

        String username = tokenService.getLoginFromToken(jwt);
        User user = (User) userService.loadUserByUsername(username);
/*        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword()));*/

        return mapper.mapToModel(user);
    }
}
