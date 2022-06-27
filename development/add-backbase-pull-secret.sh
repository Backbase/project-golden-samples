#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail

### Input user ###
read -r -p "Enter Ldap username: "  username
### Input password as hidden characters ###
read -r -s -p "Enter Ldap password: "  password

# Set the Kubernetes context
# kubectl config use-context colima

# Clean if existent
kubectl delete secret backbase-registry --ignore-not-found=true

# Create key used to pull Backbase images
kubectl create secret docker-registry backbase-registry --docker-server=repo.backbase.com/backbase-docker-releases --docker-username="$username" --docker-password="$password" --docker-email="$username"@backbase.com
