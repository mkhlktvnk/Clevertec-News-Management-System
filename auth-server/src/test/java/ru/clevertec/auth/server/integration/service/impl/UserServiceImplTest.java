package ru.clevertec.auth.server.integration.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.auth.server.builder.impl.UserTestBuilder;
import ru.clevertec.auth.server.entity.User;
import ru.clevertec.auth.server.integration.BaseIntegrationTest;
import ru.clevertec.auth.server.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceImplTest extends BaseIntegrationTest {

    private static final String USERNAME = "bob_johnson";

    @Autowired
    private UserService userService;

    @Test
    void saveUserShouldReturnUserWithNotNullId() {
        User user = UserTestBuilder.anUser()
                .withUsername("user123")
                .withPassword("password123")
                .build();

        User actual = userService.saveUser(user);

        assertThat(actual.getId()).isNotNull();
    }

    @Test
    void loadUserByUsernameShouldReturnCorrectUser() {
        User user = (User) userService.loadUserByUsername(USERNAME);

        assertThat(user.getUsername()).isEqualTo(USERNAME);
    }
}