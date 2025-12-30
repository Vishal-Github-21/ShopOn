package com.example.shopon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ShopOnApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopOnApplication.class, args);
	}
}
