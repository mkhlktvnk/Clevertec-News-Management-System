package ru.clevertec.newsresource.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.clevertec.newsresource.entity.Identifiable;

public interface PermissionService<T extends UserDetails, R extends Identifiable<?>> {
    boolean userHasPermissionToEditResource(T user, R resource);
}
