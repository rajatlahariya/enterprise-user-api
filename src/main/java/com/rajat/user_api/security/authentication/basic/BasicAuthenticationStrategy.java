package com.rajat.user_api.security.authentication.basic;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import com.rajat.user_api.security.authentication.AuthenticationStrategy;
import com.rajat.user_api.security.authentication.SecurityMode;

@Component
public class BasicAuthenticationStrategy implements AuthenticationStrategy {

    @Override
    public SecurityMode getMode() {
        return SecurityMode.BASIC;
    }

    @Override
    public SecurityFilterChain build(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .httpBasic(httpBasic -> {})
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                            "/auth/**",
                            "/swagger-ui/**",
                            "/v3/api-docs/**"
                    ).permitAll()
                    .anyRequest().authenticated()
            );

        return http.build();
    }
}