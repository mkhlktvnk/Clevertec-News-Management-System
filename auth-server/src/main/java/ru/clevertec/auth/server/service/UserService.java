package ru.clevertec.auth.server.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.clevertec.auth.server.entity.User;

public interface UserService extends UserDetailsService {
    User saveUser(User user);
}
