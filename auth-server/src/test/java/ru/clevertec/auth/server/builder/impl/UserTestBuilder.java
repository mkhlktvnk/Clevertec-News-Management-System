package ru.clevertec.auth.server.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.auth.server.builder.TestBuilder;
import ru.clevertec.auth.server.entity.Role;
import ru.clevertec.auth.server.entity.User;

import java.util.ArrayList;
import java.util.List;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "anUser")
public class UserTestBuilder implements TestBuilder<User> {
    private Long id = 0L;
    private String username = "";
    private String password = "";
    private List<Role> roles = new ArrayList<>();

    @Override
    public User build() {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setAuthorities(roles);
        return user;
    }
}
