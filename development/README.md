# Setup Backbase Local Environment

## Pre-requisites
- Colima with k3s
- Helm 3

## Steps
* Connect to Backbase VPN (we are using internal charts from harbor for the employee app)
* Run: `add-backbase-helm-repo.sh`
* Run: `add-backbase-pull-secret.sh`
* Add: `127.0.0.1 kubernetes.docker.internal` in your `/etc/hosts` file.
* Run: `helmfile sync`
