package ru.clevertec.auth.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.auth.server.entity.Role;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link Role} entities.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a role by its authority.
     *
     * @param authority the authority of the role to find
     * @return an Optional containing the Role object if found, or an empty Optional otherwise
     */
    Optional<Role> findByAuthority(String authority);
}

