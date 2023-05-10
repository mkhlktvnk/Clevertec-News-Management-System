package ru.clevertec.newsresource.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.clevertec.newsresource.entity.Identifiable;

/**
 * This interface defines methods for checking permissions related to a specific resource.
 *
 * @param <T> the type of user details object, which should extend {@link UserDetails}
 * @param <R> the type of resource, which should implement {@link Identifiable}
 */
public interface PermissionService<T extends UserDetails, R extends Identifiable<?>> {

    /**
     * Checks whether the given user has permission to edit the given resource.
     *
     * @param user the user to check permissions for
     * @param resource the resource to check permissions against
     * @return true if the user has permission to edit the resource, false otherwise
     */
    boolean userHasPermissionToEditResource(T user, R resource);
}

