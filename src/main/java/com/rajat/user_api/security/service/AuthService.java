package com.rajat.user_api.security.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;

import com.rajat.user_api.dto.request.LoginRequest;
import com.rajat.user_api.dto.response.LoginResponse;
import com.rajat.user_api.entity.User;
import com.rajat.user_api.repository.UserRepository;
import com.rajat.user_api.security.jwt.JwtProperties;
import com.rajat.user_api.security.jwt.JwtService;

@Service
public class AuthService {

    private static final String DEFAULT_ADMIN_USERNAME = "rajat";
    private static final String DEFAULT_ADMIN_PASSWORD = "rajat123";

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    public AuthService(UserRepository userRepository,
                       JwtService jwtService,
                       JwtProperties jwtProperties) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
    }

    public LoginResponse login(LoginRequest request) {

        String username = request.getUsername() == null ? "" : request.getUsername().trim();
        String password = request.getPassword() == null ? "" : request.getPassword().trim();

        if (!"rajat".equals(username) || !"rajat123".equals(password)) {
            throw new RuntimeException("Invalid login credentials");
        }

        return new LoginResponse(
                jwtService.generateAccessToken(username),
                jwtService.generateRefreshToken(username),
                "Bearer",
                jwtProperties.getExpirationMs()
        );
    }

    public LoginResponse refreshToken(String refreshToken) {

        String username = jwtService.extractUsername(refreshToken);

        if (!jwtService.isTokenValid(refreshToken, username)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        return new LoginResponse(
                jwtService.generateAccessToken(username),
                jwtService.generateRefreshToken(username),
                "Bearer",
                jwtProperties.getExpirationMs()
        );
    }
}