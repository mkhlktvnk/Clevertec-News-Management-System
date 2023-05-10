package ru.clevertec.auth.server.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.clevertec.auth.server.entity.User;

/**
 * Defines the contract for user-related operations such as creating and retrieving users.
 */
public interface UserService extends UserDetailsService {

    /**
     * Saves a new user to the system.
     *
     * @param user the user to be saved
     * @return the saved user
     */
    User saveUser(User user);
}

