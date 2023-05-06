package ru.clevertec.auth.server.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.auth.server.builder.TestBuilder;
import ru.clevertec.auth.server.entity.Role;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aRole")
public class RoleTestBuilder implements TestBuilder<Role> {
    private Long id;
    private String authority;

    @Override
    public Role build() {
        Role role = new Role();
        role.setId(id);
        role.setAuthority(authority);
        return role;
    }
}
