package com.donnytab.AllSpring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AllSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(AllSpringApplication.class, args);
    }
}
