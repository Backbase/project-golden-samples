# Setup Backbase Local Environment

## Pre-requisites
- Colima with k3s (`--with-kubernetes`)
- Helm 3
- Backbase Repository Credentials

## Steps
* Connect to Backbase VPN (we are using internal charts from harbor for the employee app)
* Run: `add-backbase-helm-repo.sh`
* Run: `add-backbase-pull-secret.sh`
* Add: `127.0.0.1 kubernetes.docker.internal` in your `/etc/hosts` file.
* Obtain the [employee-web-app-essentials](images/employee-web-app-essentials/README.md) image as it is not yet public available.
* Run: `helmfile sync`.
