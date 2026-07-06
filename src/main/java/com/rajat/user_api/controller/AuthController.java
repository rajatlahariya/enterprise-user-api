package com.rajat.user_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("AUTH CONTROLLER HIT");
    }

    @PostMapping("/login")
    public ResponseEntity<ApiSuccessResponse<LoginResponse>> login(
            @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(
                ApiResponseBuilder.success("Login successful", response)
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