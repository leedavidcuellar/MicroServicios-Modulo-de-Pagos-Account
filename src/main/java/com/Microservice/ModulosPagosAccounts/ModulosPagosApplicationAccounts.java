package com.Microservice.ModulosPagosAccounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class ModulosPagosApplicationAccounts {

	public static void main(String[] args) {
		SpringApplication.run(ModulosPagosApplicationAccounts.class, args);
	}
	/*@Bean
	public CommandLineRunner initData(AccountRepository accountRepository) {
		return (args) -> {

	}

	 */
}
