#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8081}"
AUTH_TYPE="${AUTH_TYPE:-jwt}"
USERNAME="${SMOKE_USERNAME:-rajat}"
PASSWORD="${SMOKE_PASSWORD:-rajat123}"

echo "Running smoke checks"
echo "BASE_URL=${BASE_URL}"
echo "AUTH_TYPE=${AUTH_TYPE}"

curl -fs "${BASE_URL}/actuator/health" | grep -q '"status":"UP"'
echo "Health check passed"

case "${AUTH_TYPE}" in
  jwt)
    curl -fs -X POST "${BASE_URL}/auth/login" \
      -H "Content-Type: application/json" \
      -d "{\"username\":\"${USERNAME}\",\"password\":\"${PASSWORD}\"}" | grep -q "accessToken"
    echo "JWT login smoke test passed"
    ;;

  basic)
    curl -fs -u "${USERNAME}:${PASSWORD}" "${BASE_URL}/users?size=1" > /dev/null
    echo "Basic protected endpoint smoke test passed"
    ;;

  oauth2)
    curl -fs -o /dev/null "${BASE_URL}/oauth2/authorization/google"
    echo "OAuth2 authorization entrypoint smoke test passed"
    ;;

  *)
    echo "Unsupported AUTH_TYPE=${AUTH_TYPE}"
    exit 1
    ;;
esac

echo "Smoke checks passed"
