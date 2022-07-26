#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail

: "${TOKEN_CONVERTER_URL:=http://token-converter:8080}"
: "${ACCESS_CONTROL_URL:=http://access-control:8080}"
: "${ARRANGEMENT_MANAGER_URL:=http://arrangement-manager:8080}"
: "${USER_MANAGER_URL:=http://user-manager:8080}"
: "${IDENTITY_INTEGRATION_URL:=http://identity-integration-service:8080}"
: "${IDENTITY_URL:=http://backbase-identity:8080/auth}"

TOKEN_CONVERTER_HEALTH_ENDPOINT="${TOKEN_CONVERTER_URL}/actuator/health/readiness"
ACCESS_CONTROL_HEALTH_ENDPOINT="${ACCESS_CONTROL_URL}/actuator/health/readiness"
ARRANGEMENT_MANAGER_ENDPOINT="${ARRANGEMENT_MANAGER_URL}/actuator/health/readiness"
USER_MANAGER_HEALTH_ENDPOINT="${USER_MANAGER_URL}/actuator/health/readiness"
IDENTITY_INTEGRATION_HEALTH_ENDPOINT="${IDENTITY_INTEGRATION_URL}/actuator/health/readiness"
IDENTITY_HEALTH_ENDPOINT="${IDENTITY_URL}/q/health/ready"

health_check() {
  SERVICE_NAME=$1
  HEALTH_ENDPOINT=$2
  echo "Checking ${SERVICE_NAME} health..."
  until curl -s "$HEALTH_ENDPOINT" | grep UP; do
    echo "Waiting for ${SERVICE_NAME} service on ${HEALTH_ENDPOINT}..."
    sleep 3
  done
  echo "The ${SERVICE_NAME} service is up and running"
}

health_check "token-converter" $TOKEN_CONVERTER_HEALTH_ENDPOINT
health_check "access-control" $ACCESS_CONTROL_HEALTH_ENDPOINT
health_check "arrangement-manager" $ARRANGEMENT_MANAGER_ENDPOINT
health_check "user-manager" $USER_MANAGER_HEALTH_ENDPOINT
health_check "identity-integration-service" $IDENTITY_INTEGRATION_HEALTH_ENDPOINT
health_check "identity" $IDENTITY_HEALTH_ENDPOINT

export JAVA_TOOL_OPTIONS="${JAVA_TOOL_OPTIONS} -Dspring.profiles.active=moustache-bank -Dspring.cloud.kubernetes.config.enabled=false -Dlogging.level.com.backbase.stream=DEBUG"

echo "Running product-catalog-task..."
java -jar product-catalog-task.jar
echo "Executed product-catalog-task successfully"

echo "Running legal-entity-bootstrap-task..."
java -jar legal-entity-bootstrap-task.jar
echo "Executed legal-entity-bootstrap-task successfully"
