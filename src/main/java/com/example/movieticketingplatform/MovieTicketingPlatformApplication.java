package com.example.movieticketingplatform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.example.movieticketingplatform.mapper"})
public class MovieTicketingPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieTicketingPlatformApplication.class, args);
    }

}
