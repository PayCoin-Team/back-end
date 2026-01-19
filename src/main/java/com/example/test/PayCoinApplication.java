package com.example.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class PayCoinApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayCoinApplication.class, args);
    }

}
