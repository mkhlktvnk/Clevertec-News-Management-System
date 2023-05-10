package ru.clevertec.newsresource.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "auth-server", url = "http://auth-server:9000/")
public interface AuthServerClient {

    @GetMapping("/auth/validate")
    ResponseEntity<?> validate(@RequestHeader("Authorization") String token);

}
