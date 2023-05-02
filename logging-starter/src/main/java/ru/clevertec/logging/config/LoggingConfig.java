package ru.clevertec.logging.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.logging.aspect.LoggableAspect;

@Configuration
public class LoggingConfig {

    @Bean
    public LoggableAspect loggableAspect() {
        return new LoggableAspect();
    }

}
