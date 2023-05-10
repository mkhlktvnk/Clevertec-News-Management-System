package ru.clevertec.newsresource.service.impl;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import ru.clevertec.newsresource.entity.News;
import ru.clevertec.newsresource.service.PermissionService;

/**
 * This service provides permission checks for editing news articles based on the user's role and ownership of the resource.
 */
@Service
public class NewsPermissionService implements PermissionService<User, News> {

    /**
     * Checks if the given user has permission to edit the specified news article.
     * The user must be an admin or the owner of the news article to have permission to edit it.
     *
     * @param user the user requesting permission
     * @param resource the news article to be edited
     * @return true if the user has permission to edit the news article, false otherwise
     */
    @Override
    public boolean userHasPermissionToEditResource(User user, News resource) {
        boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isNewsBelongsToUser = resource.getUsername().equals(user.getUsername());

        return  isAdmin || isNewsBelongsToUser;
    }

}

