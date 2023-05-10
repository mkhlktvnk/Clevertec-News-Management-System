package ru.clevertec.auth.server.unit.web.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import ru.clevertec.auth.server.builder.impl.UserTestBuilder;
import ru.clevertec.auth.server.entity.User;
import ru.clevertec.auth.server.web.mapper.UserMapper;
import ru.clevertec.auth.server.web.model.AuthRequest;
import ru.clevertec.auth.server.web.model.AuthResponse;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @ParameterizedTest
    @MethodSource("provideUsers")
    void mapToModelShouldMapToAuthResponse(User user) {
        AuthResponse response = mapper.mapToModel(user);

        assertThat(response.getId()).isEqualTo(user.getId());
        assertThat(response.getUsername()).isEqualTo(user.getUsername());
    }

    @ParameterizedTest
    @MethodSource("provideAuthRequests")
    void mapToEntity(AuthRequest authRequest) {
        User user = mapper.mapToEntity(authRequest);

        assertThat(user.getUsername()).isEqualTo(authRequest.getUsername());
        assertThat(user.getPassword()).isEqualTo(authRequest.getPassword());
    }

    private static Stream<User> provideUsers() {
        return Stream.of(
                UserTestBuilder.anUser()
                        .withId(1L)
                        .withUsername("user123")
                        .withPassword("password123")
                        .build(),
                UserTestBuilder.anUser()
                        .withId(2L)
                        .withUsername("user124")
                        .withPassword("password124")
                        .build(),
                UserTestBuilder.anUser()
                        .withId(3L)
                        .withUsername("user125")
                        .withPassword("password125")
                        .build()
        );
    }

    private static Stream<AuthRequest> provideAuthRequests() {
        return Stream.of(
                AuthRequest.builder()
                        .username("user123")
                        .password("password123")
                        .build(),
                AuthRequest.builder()
                        .username("user124")
                        .password("password124")
                        .build(),
                AuthRequest.builder()
                        .username("user125")
                        .password("password125")
                        .build()
        );
    }
}