package ru.clevertec.auth.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.auth.server.entity.Role;
import ru.clevertec.auth.server.entity.RoleType;
import ru.clevertec.auth.server.entity.User;
import ru.clevertec.auth.server.repository.RoleRepository;
import ru.clevertec.auth.server.repository.UserRepository;
import ru.clevertec.auth.server.service.UserService;

import java.util.ArrayList;
import java.util.Collections;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByAuthority(RoleType.SUBSCRIBER.name())
                .orElseGet(() -> {
                    Role authority = new Role();
                    authority.setAuthority(RoleType.SUBSCRIBER.name());
                    return roleRepository.save(authority);
                });
        user.setAuthorities(Collections.singletonList(role));

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(RuntimeException::new);
    }
}
