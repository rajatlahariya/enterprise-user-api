# Authorization Matrix

This project separates authentication from authorization.

Authentication answers:

```text
Who are you?
```

Authorization answers:

```text
What are you allowed to do?
```

## Token Types

| Token type | Issued by | Intended use |
|---|---|---|
| User access token | `POST /auth/login` | Human user API access |
| Client access token | `POST /auth/client-token` | Automation/service identity |
| OAuth2 session | Google OAuth2 login | Browser-based user login |

## Roles

| Role | Purpose |
|---|---|
| `ROLE_ADMIN` | Full user-management access |
| `ROLE_MANAGER` | Read-focused business access |
| `ROLE_USER` | Normal user access |
| `ROLE_CLIENT` | Machine client identity, not a human user |

## Current User API Rules

| Endpoint | ADMIN | MANAGER | USER | CLIENT |
|---|---:|---:|---:|---:|
| `GET /users` | ✅ | ✅ | ❌ | ❌ |
| `GET /users/{id}` | ✅ | ✅ | ✅ | ❌ |
| `POST /users` | ✅ | ❌ | ❌ | ❌ |
| `PUT /users/{id}` | ✅ | ❌ | ❌ | ❌ |
| `PATCH /users/{id}` | ✅ | ❌ | ❌ | ❌ |
| `DELETE /users/{id}` | ✅ | ❌ | ❌ | ❌ |

## Client Token Boundary

Client credentials tokens are valid JWTs, but they are not human user tokens.

A client token can authenticate successfully but must not automatically get access to human user APIs.

Expected behavior:

```bash
GET /users with client token -> 403 Forbidden
```

This is tested by the smoke script in JWT mode.

## SDET Test Priorities

The Rest Assured framework should include these authorization tests:

1. Missing token returns `401`.
2. Invalid token returns `401`.
3. Admin token can list users.
4. Manager token can list users.
5. Normal user token cannot list all users.
6. Client token cannot list users.
7. Admin can create/update/delete users.
8. Manager/user/client cannot create/update/delete users.
