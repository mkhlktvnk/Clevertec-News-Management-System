package ru.clevertec.newsresource.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.clevertec.newsresource.security.filter.AuthFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthFilter authFilter;

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity.csrf().disable()
                .cors().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/api/v0/news/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v0/comments/**").hasAnyRole("ADMIN", "SUBSCRIBER")
                .requestMatchers(HttpMethod.PATCH, "/api/v0/comments/**").hasAnyRole("ADMIN", "SUBSCRIBER")
                .requestMatchers(HttpMethod.DELETE, "/api/v0/comments/**").hasAnyRole("ADMIN", "SUBSCRIBER")
                .requestMatchers(HttpMethod.POST, "/api/v0/news/**").hasAnyRole("ADMIN", "JOURNALIST")
                .requestMatchers(HttpMethod.PATCH, "/api/v0/news/**").hasAnyRole("ADMIN", "JOURNALIST")
                .requestMatchers(HttpMethod.DELETE, "/api/v0/news/**").hasAnyRole("ADMIN", "JOURNALIST")
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
