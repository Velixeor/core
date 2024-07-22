package com.example.core.util;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class CoreConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
