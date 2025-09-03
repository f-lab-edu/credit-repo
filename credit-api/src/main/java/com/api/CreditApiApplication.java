package com.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.api", "com.creditcore", "com.creditexternalapi"})
@EnableJpaRepositories(basePackages = "com.creditcore.repository")
@EntityScan(basePackages = "com.creditcore.entity")
public class CreditApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditApiApplication.class, args);
	}

}
