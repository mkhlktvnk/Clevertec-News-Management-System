package ru.clevertec.newsresource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("ru.clevertec.newsresource")
public class NewsResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsResourceServerApplication.class);
    }

}
