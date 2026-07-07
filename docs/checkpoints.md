# Phase A Checkpoints

## Completed

- Checkpoint 0: Working JWT, Basic, OAuth2 CI/CD version tagged/saved.
- Checkpoint 1: Configuration cleanup completed.
- Checkpoint 2: Debug and temporary console cleanup completed.
- Checkpoint 3: OAuth2 profile isolation fixed; JWT app starts without Google credentials.
- Checkpoint 4: Docker and Jenkins deployment stabilized.
- Checkpoint 5: Public endpoints centralized in one place.
- Checkpoint 6: Jenkins now performs AUTH_TYPE-specific smoke tests after deployment.

## In this PR

- Checkpoint 7: Add reusable smoke test script for local and Jenkins validation.
- Checkpoint 8: Add v1.0 release checklist and SDET transition plan.

## Remaining before SDET mode

- Checkpoint 9: Run final Jenkins verification for `jwt`, `basic`, and `oauth2`, then tag `v1.0-auth-cicd`.


## Phase B Checkpoints

- B1: Seed realistic enterprise users for testing - completed
- B2: Pagination, sorting and advanced user filters - completed

### B2 Verification URLs

```bash
curl -i "http://localhost:8081/users?page=0&size=10&sort=firstName,asc"
curl -i "http://localhost:8081/users?role=ROLE_MANAGER&page=0&size=5&sort=username,asc"
curl -i "http://localhost:8081/users?isActive=false&page=0&size=5"
curl -i "http://localhost:8081/users?minAge=30&maxAge=40&page=0&size=10&sort=age,desc"
```


## Phase B Authorization Completion

- B3: Sorting verification - completed
- B4: Advanced filtering - completed
- B5: RBAC authorization - completed
- B6: Client token boundary enforcement - completed
- B7: SDET handoff readiness - completed

### B7 Verification Commands

```bash
AUTH_TYPE=jwt BASE_URL=http://localhost:8081 ./scripts/smoke-test.sh
AUTH_TYPE=basic BASE_URL=http://localhost:8081 ./scripts/smoke-test.sh
AUTH_TYPE=oauth2 BASE_URL=http://localhost:8081 ./scripts/smoke-test.sh
```

Expected JWT-mode checks:

- `/actuator/health` returns `UP`
- `/auth/login` returns a user JWT
- admin token can access `/users`
- client token receives `403` for `/users`
```
