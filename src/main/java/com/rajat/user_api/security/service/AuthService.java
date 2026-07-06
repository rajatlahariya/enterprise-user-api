package com.rajat.user_api.security.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rajat.user_api.dto.request.LoginRequest;
import com.rajat.user_api.dto.response.LoginResponse;
import com.rajat.user_api.entity.User;
import com.rajat.user_api.repository.UserRepository;
import com.rajat.user_api.security.jwt.JwtProperties;
import com.rajat.user_api.security.jwt.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       JwtProperties jwtProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new DisabledException("User is inactive");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return new LoginResponse(
                jwtService.generateAccessToken(user.getUsername()),
                jwtService.generateRefreshToken(user.getUsername()),
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