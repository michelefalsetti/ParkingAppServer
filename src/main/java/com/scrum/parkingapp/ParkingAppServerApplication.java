package com.scrum.parkingapp;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class ParkingAppServerApplication {

	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(ParkingAppServerApplication.class, args);
	}

	@PostConstruct
	public void checkProperties() {
		System.out.println("AES Secret Key: " + env.getProperty("aes.secret.key"));
	}
}
