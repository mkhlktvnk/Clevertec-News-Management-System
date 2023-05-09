package ru.clevertec.auth.server.integration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.auth.server.entity.Role;
import ru.clevertec.auth.server.entity.RoleType;
import ru.clevertec.auth.server.integration.BaseIntegrationTest;
import ru.clevertec.auth.server.repository.RoleRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RoleRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findByAuthorityShouldReturnCorrectAuthority() {
        Optional<Role> role = roleRepository.findByAuthority(RoleType.ROLE_JOURNALIST.name());

        assertThat(role).isNotEmpty();
        assertThat(role.get().getAuthority()).isEqualTo(RoleType.ROLE_JOURNALIST.name());
    }

}