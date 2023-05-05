package ru.clevertec.newsresource.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ru.clevertec.newsresource.builder.TestBuilder;

import java.util.ArrayList;
import java.util.List;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "anUser")
public class UserTestBuilder implements TestBuilder<User> {
    private String username = "";
    private String password = "";
    private List<SimpleGrantedAuthority> roles = new ArrayList<>();

    @Override
    public User build() {
        return new User(username, password, roles);
    }
}
