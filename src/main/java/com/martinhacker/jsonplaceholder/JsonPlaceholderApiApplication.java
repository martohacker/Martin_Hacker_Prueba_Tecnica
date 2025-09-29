package com.martinhacker.jsonplaceholder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class JsonPlaceholderApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsonPlaceholderApiApplication.class, args);
    }

}
