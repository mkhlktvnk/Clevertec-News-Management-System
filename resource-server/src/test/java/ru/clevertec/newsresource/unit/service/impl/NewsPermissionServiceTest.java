package ru.clevertec.newsresource.unit.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ru.clevertec.newsresource.builder.impl.NewsTestBuilder;
import ru.clevertec.newsresource.builder.impl.UserTestBuilder;
import ru.clevertec.newsresource.entity.News;
import ru.clevertec.newsresource.service.impl.NewsPermissionService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NewsPermissionServiceTest {

    private final NewsPermissionService newsPermissionService = new NewsPermissionService();

    @Test
    void userHasPermissionToEditResourceShouldReturnTrueWhenUsernameIsEqualToUser() {
        News news = NewsTestBuilder.aNews()
                .withUsername("user-1")
                .build();
        User user = UserTestBuilder.anUser()
                .withUsername("user-1")
                .build();

        assertThat(newsPermissionService.userHasPermissionToEditResource(user, news))
                .isTrue();
    }

    @Test
    void userHasPermissionToEditResourceShouldReturnTrueWhenUserHasAdminRole() {
        News news = NewsTestBuilder.aNews()
                .withUsername("user-1")
                .build();
        User user = UserTestBuilder.anUser()
                .withUsername("user-2")
                .withRoles(List.of(new SimpleGrantedAuthority("ADMIN")))
                .build();

        assertThat(newsPermissionService.userHasPermissionToEditResource(user, news))
                .isTrue();
    }
}