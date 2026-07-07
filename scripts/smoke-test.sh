#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8081}"
AUTH_TYPE="${AUTH_TYPE:-jwt}"
USERNAME="${SMOKE_USERNAME:-rajat}"
PASSWORD="${SMOKE_PASSWORD:-rajat123}"
CLIENT_ID="${CLIENT_AUTH_ID:-rest-assured-client}"
CLIENT_SECRET="${CLIENT_AUTH_SECRET:-secret123}"

echo "Running smoke checks"
echo "BASE_URL=${BASE_URL}"
echo "AUTH_TYPE=${AUTH_TYPE}"

assert_status() {
  local expected_status="$1"
  local method="$2"
  local url="$3"
  shift 3

  local actual_status
  actual_status=$(curl -s -o /tmp/smoke-response.json -w "%{http_code}" -X "${method}" "${url}" "$@")

  if [ "${actual_status}" != "${expected_status}" ]; then
    echo "Expected HTTP ${expected_status}, but got HTTP ${actual_status}"
    echo "URL: ${url}"
    echo "Response:"
    cat /tmp/smoke-response.json || true
    echo
    exit 1
  fi
}

extract_json_value() {
  local key="$1"
  python3 -c "import json,sys; print(json.load(sys.stdin)['data']['${key}'])"
}

curl -fs "${BASE_URL}/actuator/health" | grep -q '"status":"UP"'
echo "Health check passed"

CLIENT_TOKEN=$(curl -fs -X POST "${BASE_URL}/auth/client-token" \
  -H "Content-Type: application/json" \
  -d "{\"clientId\":\"${CLIENT_ID}\",\"clientSecret\":\"${CLIENT_SECRET}\"}" | extract_json_value "accessToken")

if [ -z "${CLIENT_TOKEN}" ] || [ "${CLIENT_TOKEN}" = "null" ]; then
  echo "Client token was not generated"
  exit 1
fi

echo "Client credentials token generation passed"

case "${AUTH_TYPE}" in
  jwt)
    USER_TOKEN=$(curl -fs -X POST "${BASE_URL}/auth/login" \
      -H "Content-Type: application/json" \
      -d "{\"username\":\"${USERNAME}\",\"password\":\"${PASSWORD}\"}" | extract_json_value "accessToken")

    if [ -z "${USER_TOKEN}" ] || [ "${USER_TOKEN}" = "null" ]; then
      echo "JWT user token was not generated"
      exit 1
    fi

    echo "JWT login smoke test passed"

    assert_status "200" "GET" "${BASE_URL}/users?page=0&size=5" \
      -H "Authorization: Bearer ${USER_TOKEN}"

    echo "Admin role authorization passed"

    assert_status "403" "GET" "${BASE_URL}/users?page=0&size=5" \
      -H "Authorization: Bearer ${CLIENT_TOKEN}"

    echo "Client token boundary check passed"
    ;;

  basic)
    assert_status "200" "GET" "${BASE_URL}/users?size=1" \
      -u "${USERNAME}:${PASSWORD}"

    echo "Basic protected endpoint smoke test passed"
    ;;

  oauth2)
    OAUTH_STATUS=$(curl -s -o /tmp/oauth-response.html -w "%{http_code}" -L "${BASE_URL}/oauth2/authorization/google")

    if [ "${OAUTH_STATUS}" != "200" ] && [ "${OAUTH_STATUS}" != "302" ]; then
      echo "OAuth2 entrypoint returned unexpected HTTP ${OAUTH_STATUS}"
      cat /tmp/oauth-response.html || true
      exit 1
    fi

    echo "OAuth2 authorization entrypoint smoke test passed"
    ;;

  *)
    echo "Unsupported AUTH_TYPE=${AUTH_TYPE}"
    exit 1
    ;;
esac

echo "Smoke checks passed"
