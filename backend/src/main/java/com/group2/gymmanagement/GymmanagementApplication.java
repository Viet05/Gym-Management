package com.group2.gymmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GymmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymmanagementApplication.class, args);
	}

}
