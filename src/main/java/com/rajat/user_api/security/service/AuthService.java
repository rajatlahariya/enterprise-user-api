package com.rajat.user_api.security.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rajat.user_api.dto.request.ClientCredentialsRequest;
import com.rajat.user_api.dto.request.LoginRequest;
import com.rajat.user_api.dto.response.LoginResponse;
import com.rajat.user_api.entity.User;
import com.rajat.user_api.repository.UserRepository;
import com.rajat.user_api.security.config.ClientCredentialsProperties;
import com.rajat.user_api.security.jwt.JwtProperties;
import com.rajat.user_api.security.jwt.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final ClientCredentialsProperties clientCredentialsProperties;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       JwtProperties jwtProperties,
                       ClientCredentialsProperties clientCredentialsProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
        this.clientCredentialsProperties = clientCredentialsProperties;
    }

    public LoginResponse login(LoginRequest request) {

        String username = request.getUsername() == null ? "" : request.getUsername().trim();
        String password = request.getPassword() == null ? "" : request.getPassword().trim();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new DisabledException("User is inactive");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return new LoginResponse(
                jwtService.generateAccessToken(user.getUsername()),
                jwtService.generateRefreshToken(user.getUsername()),
                "Bearer",
                jwtProperties.getExpirationMs()
        );
    }


    public LoginResponse generateClientToken(ClientCredentialsRequest request) {

        String clientId = request.getClientId() == null ? "" : request.getClientId().trim();
        String clientSecret = request.getClientSecret() == null ? "" : request.getClientSecret().trim();

        if (!clientId.equals(clientCredentialsProperties.getClientId())
                || !clientSecret.equals(clientCredentialsProperties.getClientSecret())) {
            throw new BadCredentialsException("Invalid client credentials");
        }

        return new LoginResponse(
                jwtService.generateAccessToken(clientId),
                null,
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