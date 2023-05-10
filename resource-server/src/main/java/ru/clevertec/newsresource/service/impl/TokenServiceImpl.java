package ru.clevertec.newsresource.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import ru.clevertec.newsresource.service.TokenService;

import java.security.interfaces.RSAPublicKey;
import java.util.List;

/**
 * Implementation of {@link TokenService} that provides methods to extract information from a JSON Web Token (JWT).
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Value("${jwt.publicKey}")
    private RSAPublicKey publicKey;

    /**
     * Extracts the user information from the JWT.
     *
     * @param token The JWT.
     * @return The user information.
     */
    @Override
    public User getUserInfoFromToken(String token) {
        String username = getUsernameFromToken(token);
        List<SimpleGrantedAuthority> authorities = getRolesFromToken(token);

        return new User(username, "", authorities);
    }

    private String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("username").toString();
    }

    private List<SimpleGrantedAuthority> getRolesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        List<String> roles = (List<String>) claims.get("roles");
        return roles.stream().map(SimpleGrantedAuthority::new).toList();
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
