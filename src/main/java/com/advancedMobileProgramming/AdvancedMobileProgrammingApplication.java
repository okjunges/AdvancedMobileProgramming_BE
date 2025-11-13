package com.advancedMobileProgramming;

import com.advancedMobileProgramming.global.util.vision.VisionProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AdvancedMobileProgrammingApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdvancedMobileProgrammingApplication.class, args);
	}

}
