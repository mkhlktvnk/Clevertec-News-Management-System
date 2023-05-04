package ru.clevertec.newsresource.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/api/v0/news/**", "/api/v0/comments/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v0/news/**").hasAnyRole("ADMIN", "JOURNALIST")
                .requestMatchers(HttpMethod.PATCH, "/api/v0/news/**").hasAnyRole("ADMIN", "JOURNALIST")
                .requestMatchers(HttpMethod.DELETE, "/api/v0/news/**").hasAnyRole("ADMIN", "JOURNALIST")
                .requestMatchers(HttpMethod.POST, "/api/v0/comments/**").hasAnyRole("ADMIN", "SUBSCRIBER")
                .requestMatchers(HttpMethod.PATCH, "/api/v0/comments/**").hasAnyRole("ADMIN", "SUBSCRIBER")
                .requestMatchers(HttpMethod.DELETE, "/api/v0/comments/**").hasAnyRole("ADMIN", "SUBSCRIBER")
                .and()
                .build();
    }

}
