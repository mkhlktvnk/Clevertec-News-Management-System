package ru.clevertec.newsresource.service.impl;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import ru.clevertec.newsresource.entity.News;
import ru.clevertec.newsresource.service.PermissionService;

@Service
public class NewsPermissionService implements PermissionService<User, News> {

    @Override
    public boolean userHasPermissionToEditResource(User user, News resource) {
        boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
        boolean isNewsBelongsToUser = resource.getUsername().equals(user.getUsername());

        return  isAdmin || isNewsBelongsToUser;
    }

}
