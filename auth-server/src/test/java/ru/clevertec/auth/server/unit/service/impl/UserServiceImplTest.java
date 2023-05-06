package ru.clevertec.auth.server.unit.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.clevertec.auth.server.builder.impl.RoleTestBuilder;
import ru.clevertec.auth.server.builder.impl.UserTestBuilder;
import ru.clevertec.auth.server.entity.Role;
import ru.clevertec.auth.server.entity.User;
import ru.clevertec.auth.server.repository.RoleRepository;
import ru.clevertec.auth.server.repository.UserRepository;
import ru.clevertec.auth.server.service.impl.UserServiceImpl;
import ru.clevertec.auth.server.service.message.MessagesSource;
import ru.clevertec.exception.handling.starter.exception.ResourceNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private static final String USERNAME = "user1";

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private MessagesSource messagesSource;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void saveUserShouldReturnSavedUserAndCallRepository() {
        User expected = UserTestBuilder.anUser().build();
        Role role = RoleTestBuilder.aRole().build();
        doReturn(Optional.of(role)).when(roleRepository).findByAuthority(any(String.class));
        doReturn(expected).when(userRepository).save(expected);

        User actual = userService.saveUser(expected);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void saveUserShouldThrowResourceNotFoundException() {
        User user = UserTestBuilder.anUser().build();
        doReturn(Optional.empty()).when(roleRepository).findByAuthority(any(String.class));

        assertThatThrownBy(() -> userService.saveUser(user))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void loadUserByUsernameShouldReturnUser() {
        User user = UserTestBuilder.anUser().build();
        doReturn(Optional.of(user)).when(userRepository).findByUsername(USERNAME);

        User actual = (User) userService.loadUserByUsername(USERNAME);

        assertThat(actual).isEqualTo(user);
    }

    @Test
    void loadUserByUsernameShouldThrowResourceNotFoundException() {
        doReturn(Optional.empty()).when(userRepository).findByUsername(USERNAME);

        assertThatThrownBy(() -> userService.loadUserByUsername(USERNAME))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}