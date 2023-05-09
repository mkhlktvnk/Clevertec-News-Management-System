package ru.clevertec.auth.server.integration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.auth.server.entity.User;
import ru.clevertec.auth.server.integration.BaseIntegrationTest;
import ru.clevertec.auth.server.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername() {
        String username = "john_doe";
        Optional<User> user = userRepository.findByUsername(username);

        assertThat(user).isNotEmpty();
        assertThat(user.get().getUsername()).isEqualTo(username);
    }
}