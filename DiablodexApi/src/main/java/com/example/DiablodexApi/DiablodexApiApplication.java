package com.example.DiablodexApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DiablodexApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiablodexApiApplication.class, args);
    }
}
