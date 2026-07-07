# Architecture

## Runtime flow

```text
Client
  |
  v
Spring Boot API
  |
  +-- Public endpoints: /auth/**, /swagger-ui/**, /v3/api-docs/**, /actuator/health, /actuator/info
  |
  +-- Protected endpoints: /users/**
  |
  v
PostgreSQL
```

## Authentication modes

The application uses a strategy-based security design selected at runtime.

| Mode | Spring profile | Strategy class | Purpose |
|---|---|---|---|
| `jwt` | `jwt` | `JwtAuthenticationStrategy` | Token-based API authentication |
| `basic` | `basic` | `BasicAuthenticationStrategy` | HTTP Basic authentication using DB users |
| `oauth2` | `oauth2` | `OAuth2AuthenticationStrategy` | Google OAuth2 login |

## Profile rule

`AUTH_TYPE` and `SPRING_PROFILES_ACTIVE` must have the same value.

Examples:

```text
AUTH_TYPE=jwt     SPRING_PROFILES_ACTIVE=jwt
AUTH_TYPE=basic   SPRING_PROFILES_ACTIVE=basic
AUTH_TYPE=oauth2  SPRING_PROFILES_ACTIVE=oauth2
```

OAuth2 Google client properties are loaded only from `application-oauth2.properties`.
JWT and Basic profiles explicitly exclude OAuth2 client auto-configuration.

## CI/CD flow

```text
GitHub
  |
  v
Jenkins Pipeline
  |
  +-- Build Jar
  +-- Build Docker Image
  +-- Deploy Container
  +-- Health Check
  +-- Smoke Test based on AUTH_TYPE
```
