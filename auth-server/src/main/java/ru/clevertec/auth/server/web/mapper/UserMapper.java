package ru.clevertec.auth.server.web.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.auth.server.entity.User;
import ru.clevertec.auth.server.web.model.AuthRequest;
import ru.clevertec.auth.server.web.model.AuthResponse;

@Mapper
public interface UserMapper {

    AuthResponse mapToModel(User user);

    User mapToEntity(AuthRequest model);
}
