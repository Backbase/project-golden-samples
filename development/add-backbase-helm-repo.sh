#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail

### Input user ###
read -r -p "Enter username (the prefix to '@backbase.com'): "  username
### Input password as hidden characters ###
read -r -s -p "Enter password: "  password

# Add backbase-charts repo
helm repo add backbase-charts https://repo.backbase.com/backbase-charts --username "$username" --password "$password" --force-update
helm repo update
