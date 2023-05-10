package ru.clevertec.auth.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.auth.server.entity.User;

import java.util.Optional;

/**
 * This interface defines the methods to access and manipulate {@link User} objects in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves an {@link Optional} {@link User} object from the database by its username.
     *
     * @param username the username of the user to retrieve
     * @return an optional user object with the given username, or an empty optional if no user is found
     */
    Optional<User> findByUsername(String username);
}

