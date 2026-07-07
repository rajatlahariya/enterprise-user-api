package com.rajat.user_api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rajat.user_api.entity.OAuthClient;
import com.rajat.user_api.entity.User;
import com.rajat.user_api.repository.OAuthClientRepository;
import com.rajat.user_api.repository.UserRepository;

@Configuration
public class DataInitializer {

    private static final String DEFAULT_ADMIN_USERNAME = "rajat";
    private static final String DEFAULT_ADMIN_PASSWORD = "rajat123";
    private static final String DEFAULT_CLIENT_ID = "rest-assured-client";
    private static final String DEFAULT_CLIENT_SECRET = "secret123";

    @Bean
    CommandLineRunner seedDefaultData(UserRepository userRepository,
                                      OAuthClientRepository oAuthClientRepository,
                                      PasswordEncoder passwordEncoder) {

        return args -> {
            seedDefaultAdminUser(userRepository, passwordEncoder);
            seedDefaultRestAssuredClient(oAuthClientRepository, passwordEncoder);
        };
    }

    private void seedDefaultAdminUser(UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {

        User user = userRepository.findByUsername(DEFAULT_ADMIN_USERNAME)
                .orElseGet(User::new);

        user.setFirstName("Rajat");
        user.setLastName("Lahariya");
        user.setEmail("rajat2496@gmail.com");
        user.setAge(29);
        user.setIsActive(true);
        user.setUsername(DEFAULT_ADMIN_USERNAME);
        user.setPassword(passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD));
        user.setRole("ROLE_ADMIN");

        userRepository.save(user);
    }

    private void seedDefaultRestAssuredClient(OAuthClientRepository oAuthClientRepository,
                                              PasswordEncoder passwordEncoder) {

        OAuthClient client = oAuthClientRepository.findByClientId(DEFAULT_CLIENT_ID)
                .orElseGet(OAuthClient::new);

        client.setClientId(DEFAULT_CLIENT_ID);
        client.setClientSecret(passwordEncoder.encode(DEFAULT_CLIENT_SECRET));
        client.setScopes("users:read,users:write");
        client.setActive(true);

        oAuthClientRepository.save(client);
    }
}
