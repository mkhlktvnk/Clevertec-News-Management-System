package ru.clevertec.newsresource.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Interface for communicating with the authentication server.
 */
@FeignClient(value = "auth-server", url = "http://auth-server:9000/")
public interface AuthServerClient {

    /**
     * Validates the given token with the authentication server.
     *
     * @param token The token to be validated.
     * @return A {@link ResponseEntity} containing the validation result.
     */
    @GetMapping("/auth/validate")
    ResponseEntity<?> validate(@RequestHeader("Authorization") String token);

}

