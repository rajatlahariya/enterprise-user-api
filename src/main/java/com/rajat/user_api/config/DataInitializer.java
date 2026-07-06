package com.rajat.user_api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rajat.user_api.entity.User;
import com.rajat.user_api.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createDefaultAdminUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            boolean userExists = userRepository.findByUsername("rajat").isPresent();

            if (!userExists) {
                User user = new User();

                user.setFirstName("Rajat");
                user.setLastName("Lahariya");
                user.setEmail("rajat2496@gmail.com");
                user.setAge(29);
                user.setIsActive(true);
                user.setUsername("rajat");
                user.setPassword(passwordEncoder.encode("rajat123"));
                user.setRole("ROLE_ADMIN");

                userRepository.save(user);
            }
            
        };
    }
}