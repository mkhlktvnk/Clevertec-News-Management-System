package ru.clevertec.newsresource.unit.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ru.clevertec.newsresource.builder.impl.CommentTestBuilder;
import ru.clevertec.newsresource.builder.impl.UserTestBuilder;
import ru.clevertec.newsresource.entity.Comment;
import ru.clevertec.newsresource.service.impl.CommentPermissionService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommentPermissionServiceTest {

    private final CommentPermissionService commentPermissionService = new CommentPermissionService();

    @Test
    void userHasPermissionToEditResourceShouldReturnTrueWhenCommentUsernameIsEqualToUser() {
        Comment comment = CommentTestBuilder.aComment()
                .withUsername("user-1")
                .build();
        User user = UserTestBuilder.anUser()
                .withUsername("user-1")
                .build();

        assertThat(commentPermissionService.userHasPermissionToEditResource(user, comment))
                .isTrue();
    }

    @Test
    void userHasPermissionToEditResourceShouldReturnTrueWhenUserHasAdminRole() {
        Comment comment = CommentTestBuilder.aComment()
                .withUsername("user-1")
                .build();
        User user = UserTestBuilder.anUser()
                .withUsername("user-2")
                .withRoles(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .build();

        assertThat(commentPermissionService.userHasPermissionToEditResource(user, comment))
                .isTrue();
    }
}