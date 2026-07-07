package com.rajat.user_api.security.authentication.jwt;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.rajat.user_api.security.authentication.AuthenticationStrategy;
import com.rajat.user_api.security.authentication.SecurityMode;
import com.rajat.user_api.security.config.PublicEndpoints;
import com.rajat.user_api.security.exception.JwtAccessDeniedHandler;
import com.rajat.user_api.security.exception.JwtAuthenticationEntryPoint;
import com.rajat.user_api.security.filter.JwtAuthenticationFilter;

@Component
public class JwtAuthenticationStrategy implements AuthenticationStrategy {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    public JwtAuthenticationStrategy(JwtAuthenticationFilter jwtAuthenticationFilter,
                                     JwtAuthenticationEntryPoint authenticationEntryPoint,
                                     JwtAccessDeniedHandler accessDeniedHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    public SecurityMode getMode() {
        return SecurityMode.JWT;
    }

    @Override
    public SecurityFilterChain build(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exception -> exception
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(PublicEndpoints.PERMIT_ALL).permitAll()
                    .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}