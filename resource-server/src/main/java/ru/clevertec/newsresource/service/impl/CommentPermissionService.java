package ru.clevertec.newsresource.service.impl;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import ru.clevertec.newsresource.entity.Comment;
import ru.clevertec.newsresource.service.PermissionService;

/**
 * Service for checking permissions related to comments.
 */
@Service
public class CommentPermissionService implements PermissionService<User, Comment> {

    /**
     * Checks if the given user has permission to edit the given comment resource.
     *
     * @param user     the user to check permissions for
     * @param resource the comment resource to check permissions against
     * @return true if the user has permission to edit the resource, false otherwise
     */
    @Override
    public boolean userHasPermissionToEditResource(User user, Comment resource) {
        boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isCommentBelongsToUser = resource.getUsername().equals(user.getUsername());

        return isAdmin || isCommentBelongsToUser;
    }

}

