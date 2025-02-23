package com.chen.config;

import feign.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FinScrapyAIFeignConfig {

    @Value("${fin-scrapy-ai.feign_client.connect_timeout}")
    private Integer CONNECT_TIMEOUT;

    @Value("${fin-scrapy-ai.feign_client.read_timeout}")
    private Integer READ_TIMEOUT;

    @Bean
    public Request.Options options() {
        return new Request.Options(CONNECT_TIMEOUT, READ_TIMEOUT);
    }
}
