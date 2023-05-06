package ru.clevertec.auth.server.unit.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import ru.clevertec.auth.server.builder.impl.UserTestBuilder;
import ru.clevertec.auth.server.entity.User;
import ru.clevertec.auth.server.model.AuthRequest;
import ru.clevertec.auth.server.model.AuthResponse;
import ru.clevertec.auth.server.service.TokenService;
import ru.clevertec.auth.server.service.UserService;
import ru.clevertec.auth.server.service.impl.AuthServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void authenticateShouldReturnTokenAndCallServices() {
        String expectedToken = "token";
        AuthRequest authRequest = AuthRequest.builder()
                .username("user123")
                .password("password123")
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword());
        doReturn(authentication).when(authenticationManager)
                .authenticate(authentication);
        doReturn(expectedToken).when(tokenService)
                .generateToken(authentication.getName(), authentication.getAuthorities());

        String actualToken = authService.authenticate(authRequest);

        verify(authenticationManager).authenticate(authentication);
        verify(tokenService).generateToken(authentication.getName(), authentication.getAuthorities());
        assertThat(actualToken).isEqualTo(expectedToken);
    }

    @Test
    void registerShouldReturnRegisteredUserAndCallRepository() {
        AuthRequest authRequest = AuthRequest.builder()
                .username("user123")
                .password("password123")
                .build();
        User user = UserTestBuilder.anUser()
                .withUsername("user123")
                .withPassword("password123")
                .build();
        doReturn(user).when(userService).saveUser(any(User.class));

        AuthResponse authResponse = authService.register(authRequest);

        verify(userService).saveUser(any(User.class));
        assertThat(authResponse.getUsername()).isEqualTo(authRequest.getUsername());
    }

    @Test
    void isTokenValidShouldCallTokenService() {
        String token = "token";
        doReturn(true).when(tokenService).isTokenValid(token);

        Boolean result = authService.isTokenValid(token);

        verify(tokenService).isTokenValid(token);
        assertThat(result).isTrue();
    }
}