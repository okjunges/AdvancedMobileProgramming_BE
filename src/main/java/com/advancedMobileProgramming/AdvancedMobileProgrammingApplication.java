package com.advancedMobileProgramming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AdvancedMobileProgrammingApplication {
	//dd
	public static void main(String[] args) {
		SpringApplication.run(AdvancedMobileProgrammingApplication.class, args);
	}

}
