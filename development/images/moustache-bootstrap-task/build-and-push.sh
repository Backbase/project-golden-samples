#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail

VERSION="${1:-3.4.0}"

mkdir -p target
mvn dependency:get -DgroupId=com.backbase.stream -DartifactId=product-catalog-task -Dversion="${VERSION}" -Dtransitive=false -Ddest=./target/product-catalog-task.jar
mvn dependency:get -DgroupId=com.backbase.stream -DartifactId=legal-entity-bootstrap-task -Dversion="${VERSION}" -Dtransitive=false -Ddest=./target/legal-entity-bootstrap-task.jar

echo "Building and pushing multi-architecture image for Stream version ${VERSION}"

docker build --platform='linux/amd64' -t harbor.backbase.eu/development/moustache-bootstrap-task:"${VERSION}"-amd64 .
docker push harbor.backbase.eu/development/moustache-bootstrap-task:"${VERSION}"-amd64

docker build --platform='linux/arm64' -t harbor.backbase.eu/development/moustache-bootstrap-task:"${VERSION}"-arm64 .
docker push harbor.backbase.eu/development/moustache-bootstrap-task:"${VERSION}"-arm64

docker manifest create harbor.backbase.eu/development/moustache-bootstrap-task:"${VERSION}" \
  --amend harbor.backbase.eu/development/moustache-bootstrap-task:"${VERSION}"-amd64 \
  --amend harbor.backbase.eu/development/moustache-bootstrap-task:"${VERSION}"-arm64

docker manifest push harbor.backbase.eu/development/moustache-bootstrap-task:"${VERSION}"
