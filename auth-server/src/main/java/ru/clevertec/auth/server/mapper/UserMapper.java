package ru.clevertec.auth.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.auth.server.entity.User;
import ru.clevertec.auth.server.model.AuthRequest;
import ru.clevertec.auth.server.model.AuthResponse;

@Mapper
public interface UserMapper {

    @Mapping(target = "authorities", ignore = true)
    AuthResponse mapToModel(User user);

    User mapToEntity(AuthRequest model);
}
