package com.rajat.user_api.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String TOKEN_TYPE_CLAIM = "token_type";
    private static final String CLIENT_TOKEN_TYPE = "client";
    private static final String USER_TOKEN_TYPE = "user";
    private static final String SCOPES_CLAIM = "scopes";

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(
                jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String username) {
        return Jwts.builder()
                .subject(username)
                .claim(TOKEN_TYPE_CLAIM, USER_TOKEN_TYPE)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpirationMs()))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateClientAccessToken(String clientId, List<String> scopes) {
        return Jwts.builder()
                .subject(clientId)
                .claim(TOKEN_TYPE_CLAIM, CLIENT_TOKEN_TYPE)
                .claim(SCOPES_CLAIM, scopes)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpirationMs()))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .claim(TOKEN_TYPE_CLAIM, USER_TOKEN_TYPE)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpirationMs()))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String subject) {
        return subject.equals(extractUsername(token))
                && !isTokenExpired(token);
    }

    public boolean isClientToken(String token) {
        return CLIENT_TOKEN_TYPE.equals(extractClaims(token).get(TOKEN_TYPE_CLAIM, String.class));
    }

    @SuppressWarnings("unchecked")
    public List<String> extractScopes(String token) {
        Object scopes = extractClaims(token).get(SCOPES_CLAIM);

        if (scopes instanceof List<?>) {
            return ((List<?>) scopes).stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .toList();
        }

        return List.of();
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
