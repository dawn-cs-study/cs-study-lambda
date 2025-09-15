package com.dawn.cs.study.lambda.md;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class MdSyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(MdSyncApplication.class, args);
    }

}
