package com.tiernebre.zone_blitz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ConfigurationPropertiesScan("com.tiernebre.tailgate")
@EnableAsync
public class TailgateApplication {

	public static void main(String[] args) {
		SpringApplication.run(TailgateApplication.class, args);
	}

}
