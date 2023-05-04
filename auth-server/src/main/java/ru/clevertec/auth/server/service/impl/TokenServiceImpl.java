package ru.clevertec.auth.server.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.auth.server.properties.JwtProperties;
import ru.clevertec.auth.server.service.TokenService;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private final JwtProperties properties;

    @Override
    public String generateToken(String username) {
        Date date = Date.from(LocalDateTime.now().plusMinutes(properties.getExpiration())
                .atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setSubject(username)
                .setExpiration(date)
                .signWith(generateSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(generateSigningKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String getLoginFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(generateSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith(BEARER)) {
            return bearer.substring(7);
        }
        return null;
    }

    private Key generateSigningKey() {
        byte[] keyBytes = properties.getSecret().getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }

}
