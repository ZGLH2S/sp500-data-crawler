package com.albez.sp500;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients
@SpringBootApplication
public class Sp500Application {

    public static void main(String[] args) {
        SpringApplication.run(Sp500Application.class, args);
    }

}
