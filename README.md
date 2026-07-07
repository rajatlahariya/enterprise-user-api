# Enterprise User API

Spring Boot REST API with PostgreSQL, JWT, Basic Authentication, Google OAuth2, Docker, and Jenkins CI/CD.

This repository is being prepared as an interview-ready Senior SDET / Automation Architect portfolio project.

## Tech Stack

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT
- Google OAuth2
- Docker
- Docker Compose
- Jenkins Pipeline
- Swagger / OpenAPI
- Actuator

## Authentication Modes

| AUTH_TYPE | Spring profile | Description |
|---|---|---|
| `jwt` | `jwt` | Token-based API authentication |
| `basic` | `basic` | HTTP Basic authentication using database user details |
| `oauth2` | `oauth2` | Google OAuth2 login |

`AUTH_TYPE` and `SPRING_PROFILES_ACTIVE` must always match.

## Runtime Variables

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

## Local Docker Run

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

## Smoke Checks

Health:

```bash
curl -i http://localhost:8081/actuator/health
```

JWT login:

```bash
curl -i -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"rajat","password":"rajat123"}'
```

Basic protected endpoint:

```bash
curl -i -u rajat:rajat123 http://localhost:8081/users?size=1
```

OAuth2 authorization entrypoint:

```bash
curl -i http://localhost:8081/oauth2/authorization/google
```


## Authorization

Authorization rules are documented in [Authorization Matrix](docs/authorization-matrix.md).

Important boundary:

```text
User JWT with ROLE_ADMIN/ROLE_MANAGER can access user-management APIs.
Client credentials JWT is a machine token and must not access human user APIs.
```

The local smoke script verifies this boundary in `AUTH_TYPE=jwt` mode.


## Jenkins CI/CD Flow

```text
GitHub → Jenkins → Build JAR → Build Docker Image → Deploy Container → Health Check → Smoke Test
```

The pipeline runs a smoke test based on the selected `AUTH_TYPE`.


## Local Smoke Script

After the application is running, use the reusable smoke script:

```bash
AUTH_TYPE=jwt BASE_URL=http://localhost:8081 ./scripts/smoke-test.sh
AUTH_TYPE=basic BASE_URL=http://localhost:8081 ./scripts/smoke-test.sh
AUTH_TYPE=oauth2 BASE_URL=http://localhost:8081 ./scripts/smoke-test.sh
```

## v1.0 Release

Before tagging the project, follow the [v1.0 Release Checklist](docs/release-checklist.md).

After `v1.0-auth-cicd`, development switches to the SDET phase documented in [SDET Transition Plan](docs/sdet-transition.md).

## Documentation

- [Architecture](docs/architecture.md)
- [Phase A Checkpoints](docs/checkpoints.md)
- [v1.0 Release Checklist](docs/release-checklist.md)
- [SDET Transition Plan](docs/sdet-transition.md)
- [Authorization Matrix](docs/authorization-matrix.md)


## Client Credentials Token

This project also exposes a machine-to-machine token endpoint for automation and service-to-service testing.

```http
POST /auth/client-token
```

Request:

```json
{
  "clientId": "rest-assured-client",
  "clientSecret": "secret123"
}
```

Response contains a JWT access token that can be used as a Bearer token for protected APIs.

Environment variables:

| Variable | Default |
|---|---|
| `CLIENT_AUTH_ID` | `rest-assured-client` |
| `CLIENT_AUTH_SECRET` | `secret123` |

This endpoint is useful for Rest Assured tests because it avoids browser-based OAuth2 login while still validating Bearer token based access.

