package ru.clevertec.newsresource.service;

import org.springframework.security.core.userdetails.User;

public interface TokenService {
    User getUserInfoFromToken(String token);
}
