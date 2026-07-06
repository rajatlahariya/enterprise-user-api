package com.rajat.user_api.security.authentication;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

public interface AuthenticationStrategy {

    SecurityMode getMode();

    SecurityFilterChain build(HttpSecurity http) throws Exception;
}