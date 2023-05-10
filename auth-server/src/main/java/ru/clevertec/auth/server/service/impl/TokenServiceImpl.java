package ru.clevertec.auth.server.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import ru.clevertec.auth.server.properties.JwtProperties;
import ru.clevertec.auth.server.service.TokenService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Implementation of {@link TokenService} interface that provides functionality
 * for generating and validating JWT tokens.
 */
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JwtProperties properties;

    /**
     * Generates a new JWT token for the given username and collection of roles.
     *
     * @param username the username to include in the token
     * @param roles    the roles to include in the token
     * @return the generated JWT token
     */
    @Override
    public String generateToken(String username, Collection<? extends GrantedAuthority> roles) {
        Date date = Date.from(LocalDateTime.now().plusMinutes(properties.getExpiration())
                .atZone(ZoneId.systemDefault()).toInstant());

        Claims claims = generateClaims(username, roles);

        return Jwts.builder()
                .setSubject(username)
                .setClaims(claims)
                .setExpiration(date)
                .signWith(properties.getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    /**
     * Validates the given JWT token.
     *
     * @param token the JWT token to validate
     * @return {@code true} if the token is valid, {@code false} otherwise
     */
    @Override
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(properties.getPrivateKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private Claims generateClaims(String username, Collection<? extends GrantedAuthority> roles) {
        Claims claims = Jwts.claims();
        claims.put("username", username);
        claims.put("roles", getRolesValues(roles));
        return claims;
    }

    private List<String> getRolesValues(Collection<? extends GrantedAuthority> roles) {
        return roles.stream().map(GrantedAuthority::getAuthority).toList();
    }
}

