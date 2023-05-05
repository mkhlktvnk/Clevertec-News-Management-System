package ru.clevertec.auth.server.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.auth.server.entity.Role;
import ru.clevertec.auth.server.entity.RoleType;
import ru.clevertec.auth.server.repository.RoleRepository;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;

    private boolean alreadySetup = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        createRoleIfNotFound(RoleType.ROLE_ADMIN.name());
        createRoleIfNotFound(RoleType.ROLE_JOURNALIST.name());
        createRoleIfNotFound(RoleType.ROLE_SUBSCRIBER.name());
        alreadySetup = true;
    }

    @Transactional
    Role createRoleIfNotFound(String name) {
        return roleRepository.findByAuthority(name)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setAuthority(name);
                    return roleRepository.save(role);
                });
    }

}
