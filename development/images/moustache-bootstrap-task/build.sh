#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail

VERSION="${1:-2.82.0}"

mkdir -p target
mvn dependency:get -DgroupId=com.backbase.stream -DartifactId=product-catalog-task -Dversion="${VERSION}" -Dtransitive=false -Ddest=./target/product-catalog-task.jar
mvn dependency:get -DgroupId=com.backbase.stream -DartifactId=legal-entity-bootstrap-task -Dversion="${VERSION}" -Dtransitive=false -Ddest=./target/legal-entity-bootstrap-task.jar

docker build -t harbor.backbase.eu/development/moustache-bootstrap-task:"${VERSION}" .
docker tag harbor.backbase.eu/development/moustache-bootstrap-task:"${VERSION}" moustache-bootstrap-task:latest
