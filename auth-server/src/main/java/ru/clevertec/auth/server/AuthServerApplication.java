package ru.clevertec.auth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.clevertec.auth.server.properties.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class);
    }

}
