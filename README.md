# Enterprise User API

Spring Boot REST API with PostgreSQL, JWT, Basic Authentication, Google OAuth2, Docker, and Jenkins CI/CD.

## Authentication Modes

| AUTH_TYPE | Spring profile | Description |
|---|---|---|
| `jwt` | `jwt` | Token-based API authentication |
| `basic` | `basic` | HTTP Basic authentication using database user details |
| `oauth2` | `oauth2` | Google OAuth2 login |

## Required runtime variables

Common variables:

```bash
DB_URL=jdbc:postgresql://enterprise-user-api-db:5432/automationdb
DB_USERNAME=postgres
DB_PASSWORD=postgres
AUTH_TYPE=jwt
SPRING_PROFILES_ACTIVE=jwt
```

OAuth2 variables, required only for `AUTH_TYPE=oauth2`:

```bash
GOOGLE_CLIENT_ID=<google-client-id>
GOOGLE_CLIENT_SECRET=<google-client-secret>
```

## Local Docker run

```bash
docker run -d \
  --name enterprise-user-api \
  --network user-api_enterprise-user-api-network \
  -p 8081:8081 \
  -e DB_URL=jdbc:postgresql://enterprise-user-api-db:5432/automationdb \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=postgres \
  -e AUTH_TYPE=jwt \
  -e SPRING_PROFILES_ACTIVE=jwt \
  enterprise-user-api:latest
```

## Smoke checks

```bash
curl -i http://localhost:8081/actuator/health
curl -i -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"rajat","password":"rajat123"}'
```

## CI/CD flow

```text
GitHub → Jenkins → Build JAR → Build Docker image → Deploy container → Health check
```
