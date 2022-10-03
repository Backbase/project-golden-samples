#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail

### Input user ###
read -r -p "Enter username (the prefix to '@backbase.com'): " username
### Input password as hidden characters ###
read -r -s -p "Enter password: " password

# Adding Backbase OCI repo for Topstack
helm registry login --username "$username" --password "$password" repo.backbase.com

# Adding backbase-charts repo
helm repo add backbase-charts https://repo.backbase.com/backbase-charts --username "$username" --password "$password" --force-update
helm repo update

# Set the Kubernetes context
# kubectl config use-context colima

# Clean if existent
kubectl delete secret backbase-registry --ignore-not-found=true
# Create key used to pull Backbase images
kubectl create secret docker-registry backbase-registry --docker-server=repo.backbase.com/backbase-docker-releases --docker-username="$username" --docker-password="$password" --docker-email="$username"@backbase.com

: ${ENABLE_INTERNAL:=false}
if $ENABLE_INTERNAL; then
  # Clean if existent
  kubectl delete secret harbor-registry --ignore-not-found=true
  # Create key used to pull Backbase images
  kubectl create secret docker-registry harbor-registry --docker-server=harbor.backbase.eu --docker-username="$username" --docker-password="$password" --docker-email="$username"@backbase.com
fi
