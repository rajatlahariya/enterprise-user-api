package com.rajat.user_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.rajat.user_api.dto.request.LoginRequest;
import com.rajat.user_api.dto.request.RefreshTokenRequest;
import com.rajat.user_api.dto.response.LoginResponse;
import com.rajat.user_api.response.ApiResponseBuilder;
import com.rajat.user_api.response.ApiSuccessResponse;
import com.rajat.user_api.security.jwt.JwtProperties;
import com.rajat.user_api.security.jwt.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          JwtProperties jwtProperties) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiSuccessResponse<LoginResponse>> login(
            @RequestBody LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        ));

        String username = authentication.getName();

        LoginResponse response = new LoginResponse(
                jwtService.generateAccessToken(username),
                jwtService.generateRefreshToken(username),
                "Bearer",
                jwtProperties.getExpirationMs()
        );

        return ResponseEntity.ok(
                ApiResponseBuilder.success("Login successful", response)
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiSuccessResponse<LoginResponse>> refreshToken(
            @RequestBody RefreshTokenRequest request) {

        String username = jwtService.extractUsername(request.getRefreshToken());

        if (!jwtService.isTokenValid(request.getRefreshToken(), username)) {
            throw new RuntimeException("Invalid refresh token");
        }

        LoginResponse response = new LoginResponse(
                jwtService.generateAccessToken(username),
                jwtService.generateRefreshToken(username),
                "Bearer",
                jwtProperties.getExpirationMs()
        );

        return ResponseEntity.ok(
                ApiResponseBuilder.success("Token refreshed successfully", response)
        );
    }
}