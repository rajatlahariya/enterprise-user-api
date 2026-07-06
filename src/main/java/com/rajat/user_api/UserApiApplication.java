package com.rajat.user_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserApiApplication {

	public static void main(String[] args) {
		
		System.out.println(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder()
		        .encode("rajat123"));
		SpringApplication.run(UserApiApplication.class, args);
	}

}
