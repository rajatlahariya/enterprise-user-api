package com.rajat.user_api.security.config;

public final class PublicEndpoints {

    private PublicEndpoints() {
    }

    public static final String[] PERMIT_ALL = {
            "/auth/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/actuator/health",
            "/actuator/info"
    };
}
