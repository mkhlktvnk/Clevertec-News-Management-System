package ru.clevertec.newsresource.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.clevertec.newsresource.client.AuthServerClient;
import ru.clevertec.newsresource.security.constant.AuthConstant;
import ru.clevertec.newsresource.jwt.JwtParser;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final AuthServerClient authServerClient;
    private final JwtParser jwtParser;

    @Override
    @SneakyThrows
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)  {
        String token = getTokenFromRequest(request);
        if (token != null) {
            ResponseEntity<?> validationResponse = authServerClient.validate(token);
            if (validationResponse.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                User user = jwtParser.getUserInfoFromToken(token);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AuthConstant.AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith(AuthConstant.BEARER)) {
            return bearer.substring(7);
        }
        return null;
    }
}
