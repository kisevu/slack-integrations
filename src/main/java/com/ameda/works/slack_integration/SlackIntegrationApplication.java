package com.ameda.works.slack_integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.ameda.works.*","org.springdoc"})
public class SlackIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlackIntegrationApplication.class, args);
	}

}
