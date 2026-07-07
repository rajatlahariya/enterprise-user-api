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
    private static final String TEST_USER_PASSWORD = "Password@123";
    private static final int REQUIRED_TEST_USERS = 200;

    private static final String[] FIRST_NAMES = {
            "Aarav", "Vivaan", "Aditya", "Arjun", "Sai", "Reyansh", "Ayaan", "Krishna", "Ishaan", "Shaurya",
            "Ananya", "Diya", "Aadhya", "Avni", "Myra", "Isha", "Anika", "Saanvi", "Kavya", "Meera",
            "Rohan", "Rahul", "Karan", "Nikhil", "Varun", "Siddharth", "Kabir", "Aryan", "Yash", "Dev",
            "Priya", "Neha", "Sneha", "Pooja", "Riya", "Nisha", "Shreya", "Tanvi", "Aditi", "Kriti"
    };

    private static final String[] LAST_NAMES = {
            "Sharma", "Verma", "Gupta", "Singh", "Patel", "Mehta", "Mishra", "Kapoor", "Jain", "Malhotra",
            "Reddy", "Nair", "Iyer", "Rao", "Kumar", "Chopra", "Bansal", "Agarwal", "Joshi", "Saxena"
    };

    @Bean
    CommandLineRunner seedDefaultData(UserRepository userRepository,
                                      OAuthClientRepository oAuthClientRepository,
                                      PasswordEncoder passwordEncoder) {

        return args -> {
            seedDefaultAdminUser(userRepository, passwordEncoder);
            seedDefaultRestAssuredClient(oAuthClientRepository, passwordEncoder);
            seedEnterpriseTestUsers(userRepository, passwordEncoder);
        };
    }

    private void seedDefaultAdminUser(UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {

        User user = userRepository.findByUsername(DEFAULT_ADMIN_USERNAME)
                .orElseGet(User::new);

        user.setFirstName("Aarav");
        user.setLastName("Sharma");
        user.setEmail("demo.user@example.test");
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
        client.setScopes("automation:read,automation:write");
        client.setActive(true);

        oAuthClientRepository.save(client);
    }

    private void seedEnterpriseTestUsers(UserRepository userRepository,
                                         PasswordEncoder passwordEncoder) {

        long existingSeedUsers = userRepository.findAll().stream()
                .filter(user -> user.getUsername() != null && user.getUsername().startsWith("seed."))
                .count();

        if (existingSeedUsers >= REQUIRED_TEST_USERS) {
            return;
        }

        for (int i = 1; i <= REQUIRED_TEST_USERS; i++) {
            String firstName = FIRST_NAMES[(i - 1) % FIRST_NAMES.length];
            String lastName = LAST_NAMES[(i - 1) % LAST_NAMES.length];
            String username = buildUsername(firstName, lastName, i);

            if (userRepository.findByUsername(username).isPresent()) {
                continue;
            }

            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(username);
            user.setEmail(username + "@enterprise.test");
            user.setAge(21 + (i % 35));
            user.setIsActive(i % 10 != 0);
            user.setRole(resolveRole(i));
            user.setPassword(passwordEncoder.encode(TEST_USER_PASSWORD));

            userRepository.save(user);
        }
    }

    private String buildUsername(String firstName, String lastName, int sequence) {
        return "seed." + firstName.toLowerCase() + "." + lastName.toLowerCase() + "." + sequence;
    }

    private String resolveRole(int sequence) {
        if (sequence <= 10) {
            return "ROLE_ADMIN";
        }

        if (sequence <= 50) {
            return "ROLE_MANAGER";
        }

        return "ROLE_USER";
    }
}
