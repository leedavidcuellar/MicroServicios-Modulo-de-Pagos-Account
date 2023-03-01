package com.Microservice.ModulosPagosAccounts;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
@Configuration
public class AppConfig {
    @Bean("clientRest")
    public RestTemplate registrarRestTemplate(){
        return new RestTemplate();
    }
}

@Configuration
@EnableScheduling
class consumerConfig {}
