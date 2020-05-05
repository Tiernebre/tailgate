package com.tiernebre.tailgate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.tiernebre.tailgate")
public class TailgateApplication {

	public static void main(String[] args) {
		SpringApplication.run(TailgateApplication.class, args);
	}

}
