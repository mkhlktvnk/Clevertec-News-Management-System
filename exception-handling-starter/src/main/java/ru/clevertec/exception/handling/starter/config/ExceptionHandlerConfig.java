package ru.clevertec.exception.handling.starter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.exception.handling.starter.handler.GlobalExceptionHandler;

@Configuration
public class ExceptionHandlerConfig {

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

}
