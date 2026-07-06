package com.rajat.user_api.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.rajat.user_api.security.authentication.SecurityMode;

@Component
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    private SecurityMode mode = SecurityMode.BASIC;

    public SecurityMode getMode() {
        return mode;
    }

    public void setMode(SecurityMode mode) {
        this.mode = mode;
    }
}