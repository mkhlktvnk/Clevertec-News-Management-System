package ru.clevertec.newsresource.client;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.clevertec.newsresource.web.dto.AuthResponseDto;

@FeignClient(value = "auth-server", url = "http://localhost:9000/")
public interface AuthServerClient {

    @GetMapping("/validate")
    AuthResponseDto validate(@RequestHeader("Authorization") String token);

}
