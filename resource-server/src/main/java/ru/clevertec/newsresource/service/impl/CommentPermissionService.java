package ru.clevertec.newsresource.service.impl;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import ru.clevertec.newsresource.entity.Comment;
import ru.clevertec.newsresource.service.PermissionService;

@Service
public class CommentPermissionService implements PermissionService<User, Comment> {

    @Override
    public boolean userHasPermissionToEditResource(User user, Comment resource) {
        boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isCommentBelongsToUser = resource.getUsername().equals(user.getUsername());

        return isAdmin || isCommentBelongsToUser;
    }

}
