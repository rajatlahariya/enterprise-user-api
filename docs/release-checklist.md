# v1.0 Release Checklist

Use this checklist before tagging `v1.0-auth-cicd`.

## Required Jenkins Builds

Run the same Jenkins pipeline three times:

| Build | AUTH_TYPE | Expected result |
|---|---|---|
| 1 | `jwt` | Health check passes and JWT login smoke test passes |
| 2 | `basic` | Health check passes and Basic protected endpoint smoke test passes |
| 3 | `oauth2` | Health check passes and Google OAuth2 authorization endpoint smoke test passes |

## Manual Verification

JWT:

```bash
curl -i -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"rajat","password":"rajat123"}'
```

Basic:

```bash
curl -i -u rajat:rajat123 http://localhost:8081/users?size=1
```

OAuth2:

```bash
curl -i http://localhost:8081/oauth2/authorization/google
```

Health:

```bash
curl -i http://localhost:8081/actuator/health
```

Swagger:

```text
http://localhost:8081/swagger-ui.html
```

## Repository Hygiene

Before tagging:

- No secrets committed.
- `application.properties` does not contain Google client credentials.
- OAuth2 credentials are injected by Jenkins credentials.
- `.dockerignore` is present.
- Jenkins build is green for `jwt`, `basic`, and `oauth2`.
- README reflects the actual setup.

## Tag

```bash
git tag v1.0-auth-cicd
git push origin v1.0-auth-cicd
```
