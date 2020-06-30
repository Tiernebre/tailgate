package com.tiernebre.zone_blitz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ConfigurationPropertiesScan("com.tiernebre.zone_blitz")
@EnableAsync
public class ZoneBlitzApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZoneBlitzApplication.class, args);
	}

}
