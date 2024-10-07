package com.himi.love;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HimiLoveApplication {

    public static void main(String[] args) {
        SpringApplication.run(HimiLoveApplication.class, args);
    }
}
