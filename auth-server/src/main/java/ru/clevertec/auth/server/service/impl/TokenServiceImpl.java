package ru.clevertec.auth.server.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import ru.clevertec.auth.server.properties.JwtProperties;
import ru.clevertec.auth.server.service.TokenService;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JwtProperties properties;

    @Override
    public String generateToken(String username, Collection<? extends GrantedAuthority> roles) {
        Date date = Date.from(LocalDateTime.now().plusMinutes(properties.getExpiration())
                .atZone(ZoneId.systemDefault()).toInstant());

        Claims claims = generateClaims(username, roles);
        Key signingKey = generateSigningKeyBasedOnPrivateKey();

        return Jwts.builder()
                .setSubject(username)
                .setClaims(claims)
                .setExpiration(date)
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(generateSigningKeyBasedOnPrivateKey())
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
        claims.put("roles", roles);
        return claims;
    }

    private Key generateSigningKeyBasedOnPrivateKey() {
        byte[] keyBytes = properties.getPrivateKey().getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }
}
