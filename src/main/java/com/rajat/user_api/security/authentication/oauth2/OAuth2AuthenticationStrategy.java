package com.rajat.user_api.security.authentication.oauth2;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import com.rajat.user_api.security.authentication.AuthenticationStrategy;
import com.rajat.user_api.security.authentication.SecurityMode;
import com.rajat.user_api.security.config.PublicEndpoints;

@Component
public class OAuth2AuthenticationStrategy implements AuthenticationStrategy {

    @Override
    public SecurityMode getMode() {
        return SecurityMode.OAUTH2;
    }

    @Override
    public SecurityFilterChain build(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PublicEndpoints.PERMIT_ALL).permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> {});

        return http.build();
    }
}