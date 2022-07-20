#!/usr/bin/env sh

java -jar target/product-catalog-task.jar --spring.profiles.active=moustache-bank --spring.cloud.kubernetes.config.enabled=false
java -jar target/legal-entity-bootstrap-task.jar --spring.profiles.active=moustache-bank --spring.cloud.kubernetes.config.enabled=false
