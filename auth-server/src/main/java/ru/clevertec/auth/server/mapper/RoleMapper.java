package ru.clevertec.auth.server.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.auth.server.entity.Role;
import ru.clevertec.auth.server.model.RoleModel;

@Mapper
public interface RoleMapper {
    RoleModel mapToModel(Role entity);
}
