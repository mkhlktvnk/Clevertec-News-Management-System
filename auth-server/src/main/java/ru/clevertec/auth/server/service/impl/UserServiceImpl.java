package ru.clevertec.auth.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.auth.server.entity.Role;
import ru.clevertec.auth.server.entity.RoleType;
import ru.clevertec.auth.server.entity.User;
import ru.clevertec.auth.server.repository.RoleRepository;
import ru.clevertec.auth.server.repository.UserRepository;
import ru.clevertec.auth.server.service.UserService;
import ru.clevertec.auth.server.service.message.MessagesSource;
import ru.clevertec.auth.server.service.message.RoleMessageKey;
import ru.clevertec.auth.server.service.message.UserMessageKey;
import ru.clevertec.exception.handling.starter.exception.ResourceNotFoundException;

import java.util.Collections;

/**
 * Implementation of the {@link UserService} interface that interacts with the {@link UserRepository}
 * and {@link RoleRepository} to manage {@link User} entities and their roles.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MessagesSource messagesSource;

    /**
     * Saves a user in the database by encoding their password and adding a default role of ROLE_SUBSCRIBER.
     *
     * @param user the user to save
     * @return the saved user
     * @throws ResourceNotFoundException if the ROLE_SUBSCRIBER role is not found in the database
     */
    @Override
    @Transactional
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByAuthority(RoleType.ROLE_SUBSCRIBER.name())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(RoleMessageKey.ROLE_NOT_FOUND_BY_NAME,
                                RoleType.ROLE_SUBSCRIBER.name())
                ));

        user.setAuthorities(Collections.singletonList(role));

        return userRepository.save(user);
    }

    /**
     * Loads a user by their username.
     *
     * @param username the username to search for
     * @return the user details of the found user
     * @throws ResourceNotFoundException if a user with the given username is not found in the database
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(UserMessageKey.USER_NOT_FOUND_BY_USERNAME, username)
                ));
    }
}

