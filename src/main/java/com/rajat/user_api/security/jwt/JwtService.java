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
    private static final String TOKEN_USE_CLAIM = "token_use";
    private static final String CLIENT_TOKEN_TYPE = "client";
    private static final String USER_TOKEN_TYPE = "user";
    private static final String ACCESS_TOKEN_USE = "access";
    private static final String REFRESH_TOKEN_USE = "refresh";
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
                .claim(TOKEN_USE_CLAIM, ACCESS_TOKEN_USE)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpirationMs()))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateClientAccessToken(String clientId, List<String> scopes) {
        return Jwts.builder()
                .subject(clientId)
                .claim(TOKEN_TYPE_CLAIM, CLIENT_TOKEN_TYPE)
                .claim(TOKEN_USE_CLAIM, ACCESS_TOKEN_USE)
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
                .claim(TOKEN_USE_CLAIM, REFRESH_TOKEN_USE)
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

    public boolean isUserAccessToken(String token) {
        Claims claims = extractClaims(token);
        return USER_TOKEN_TYPE.equals(claims.get(TOKEN_TYPE_CLAIM, String.class))
                && ACCESS_TOKEN_USE.equals(claims.get(TOKEN_USE_CLAIM, String.class));
    }

    public boolean isClientAccessToken(String token) {
        Claims claims = extractClaims(token);
        return CLIENT_TOKEN_TYPE.equals(claims.get(TOKEN_TYPE_CLAIM, String.class))
                && ACCESS_TOKEN_USE.equals(claims.get(TOKEN_USE_CLAIM, String.class));
    }

    public boolean isRefreshToken(String token) {
        Claims claims = extractClaims(token);
        return USER_TOKEN_TYPE.equals(claims.get(TOKEN_TYPE_CLAIM, String.class))
                && REFRESH_TOKEN_USE.equals(claims.get(TOKEN_USE_CLAIM, String.class));
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
