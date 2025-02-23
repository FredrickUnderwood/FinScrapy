package com.chen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FinScrapyAIApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinScrapyAIApplication.class, args);
    }
}
