package ru.clevertec.auth.server.web.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.auth.server.entity.Role;
import ru.clevertec.auth.server.web.model.RoleModel;

@Mapper
public interface RoleMapper {
    RoleModel mapToModel(Role entity);
}
