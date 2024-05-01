package com.app.parker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ParkerApplication {
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public UserData userData() {return new UserData();}

	public static void main(String[] args) {
		SpringApplication.run(ParkerApplication.class, args);
	}

}
