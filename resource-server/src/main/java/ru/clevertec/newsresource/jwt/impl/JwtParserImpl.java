package ru.clevertec.newsresource.jwt.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import ru.clevertec.newsresource.jwt.JwtParser;

import java.security.interfaces.RSAPublicKey;
import java.util.List;

/**
 * Implementation of {@link JwtParser} that provides methods to extract information from a JSON Web Token (JWT).
 */
@Service
public class JwtParserImpl implements JwtParser {

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

    /**
     * Extracts the username from token
     *
     * @param token The JWT.
     * @return Extracted username.
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("username").toString();
    }

    /**
     * Extracts the roles from token
     *
     * @param token The JWT.
     * @return Extracted authorities.
     */
    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        List<String> roles = (List<String>) claims.get("roles");
        return roles.stream().map(SimpleGrantedAuthority::new).toList();
    }

    /**
     * Extracts the claims from token
     *
     * @param token The JWT.
     * @return Extracted claims.
     */
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
