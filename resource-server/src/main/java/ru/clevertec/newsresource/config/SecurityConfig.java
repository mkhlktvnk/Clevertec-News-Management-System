package ru.clevertec.newsresource.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.clevertec.newsresource.filter.AuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthFilter filter) {
        return httpSecurity.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/api/v0/news/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v0/news/**").fullyAuthenticated()
                .requestMatchers(HttpMethod.PATCH, "/api/v0/comments/**").hasAnyRole("ADMIN", "SUBSCRIBER")
                .requestMatchers(HttpMethod.DELETE, "/api/v0/comments/**").hasAnyRole("ADMIN", "SUBSCRIBER")
                .requestMatchers(HttpMethod.PATCH, "/api/v0/news/**").hasAnyRole("ADMIN", "JOURNALIST")
                .requestMatchers(HttpMethod.DELETE, "/api/v0/news/**").hasAnyRole("ADMIN", "JOURNALIST")
                .and()
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
