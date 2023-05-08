package ru.clevertec.auth.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.auth.server.entity.User;
import ru.clevertec.auth.server.model.AuthRequest;
import ru.clevertec.auth.server.model.AuthResponse;

@Mapper
public interface UserMapper {

    AuthResponse mapToModel(User user);

    User mapToEntity(AuthRequest model);
}
