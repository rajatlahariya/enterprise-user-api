# SDET Transition Plan

After `v1.0-auth-cicd`, backend feature work pauses. The next phase is to test this system as a Senior SDET.

## Next Repository

Create a separate repository:

```text
enterprise-api-tests
```

## Framework Target

The API automation framework will cover:

- Rest Assured
- TestNG or JUnit 5
- Request and response specifications
- JWT token manager
- Basic Auth support
- OAuth2 entrypoint checks
- POJO serialization and deserialization
- JSON schema validation
- Data-driven tests
- Environment configuration
- Parallel execution
- Allure reporting
- Jenkins integration

## First API Test Suite

Minimum v1 test coverage:

| Area | Tests |
|---|---|
| Health | `/actuator/health` returns UP |
| JWT Login | valid login returns access and refresh tokens |
| JWT Negative | invalid login returns unauthorized/error response |
| Basic Auth | protected endpoint succeeds with valid basic credentials |
| Protected API | request without token is rejected |
| Users API | list users, create user, update user, patch user, delete user |

## CI Target

Final CI flow:

```text
Build API → Deploy API → Health Check → Run Rest Assured Tests → Publish Report
```
