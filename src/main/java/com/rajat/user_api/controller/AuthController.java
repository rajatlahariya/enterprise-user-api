package com.rajat.user_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rajat.user_api.dto.request.ClientCredentialsRequest;
import com.rajat.user_api.dto.request.LoginRequest;
import com.rajat.user_api.dto.request.RefreshTokenRequest;
import com.rajat.user_api.dto.response.LoginResponse;
import com.rajat.user_api.response.ApiResponseBuilder;
import com.rajat.user_api.response.ApiSuccessResponse;
import com.rajat.user_api.security.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiSuccessResponse<LoginResponse>> login(
            @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(
                ApiResponseBuilder.success("Login successful", response)
        );
    }


    @PostMapping("/client-token")
    public ResponseEntity<ApiSuccessResponse<LoginResponse>> clientToken(
            @RequestBody ClientCredentialsRequest request) {

        LoginResponse response = authService.generateClientToken(request);

        return ResponseEntity.ok(
                ApiResponseBuilder.success("Client token generated successfully", response)
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiSuccessResponse<LoginResponse>> refreshToken(
            @RequestBody RefreshTokenRequest request) {

        LoginResponse response = authService.refreshToken(request.getRefreshToken());

        return ResponseEntity.ok(
                ApiResponseBuilder.success("Token refreshed successfully", response)
        );
    }
}
