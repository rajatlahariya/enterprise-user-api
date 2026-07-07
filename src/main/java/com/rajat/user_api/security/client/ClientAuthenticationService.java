package com.rajat.user_api.security.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rajat.user_api.entity.OAuthClient;
import com.rajat.user_api.repository.OAuthClientRepository;

@Service
public class ClientAuthenticationService {

    private final OAuthClientRepository oAuthClientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientAuthenticationService(OAuthClientRepository oAuthClientRepository,
                                       PasswordEncoder passwordEncoder) {
        this.oAuthClientRepository = oAuthClientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public OAuthClient authenticate(String clientId, String clientSecret) {

        OAuthClient client = oAuthClientRepository.findByClientId(clientId)
                .orElseThrow(() -> new BadCredentialsException("Invalid client credentials"));

        if (!Boolean.TRUE.equals(client.getActive())) {
            throw new DisabledException("OAuth client is inactive");
        }

        if (!passwordEncoder.matches(clientSecret, client.getClientSecret())) {
            throw new BadCredentialsException("Invalid client credentials");
        }

        return client;
    }

    public List<String> getScopes(OAuthClient client) {
        if (client.getScopes() == null || client.getScopes().isBlank()) {
            return List.of();
        }

        return Arrays.stream(client.getScopes().split(","))
                .map(String::trim)
                .filter(scope -> !scope.isBlank())
                .toList();
    }
}
