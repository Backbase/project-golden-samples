# Setup Backbase Local Environment

## Pre-requisites
- Colima with k3s 
  * `colima kubernetes start`
- Helmfile
  * `brew install helmfile`
- Kubectl or K9s
  * `brew install k9s`
- Backbase Repository Credentials

## Steps
* Run: `add-backbase-helm-repo.sh`
* Run: `add-backbase-pull-secret.sh`
* Add: `127.0.0.1 kubernetes.docker.internal` in your `/etc/hosts` file.
* Obtain the [employee-web-app-essentials](images/employee-web-app-essentials/README.md) image as it is not yet public available.
* Obtain the [moustache-bootstrap-task](images/moustache-bootstrap-task/README.md) image as it is not public available.
* Run: `helmfile sync`.

> Grab a coffee and wait for a few minutes, so everything can start. I recommend installing `k9s` to monitor the status of the pods.

## Infra Installed
- MySQL
- ActiveMQ
- Nginx Ingress Controller
- Spring Boot Admin Server

## Backbase Services Available
- Edge
- Identity Server
- Identity Integration
- Token Converter
- Access Control
- Arrangement Manager
- User Manager

## Endpoints
Once your environment is up and running you can access it using the following URLs:

- Employee Web Portal: http://kubernetes.docker.internal/
  * Employee Admin Credentials: `admin` / `admin`
- Identity: http://kubernetes.docker.internal/auth
  * Realm Admin Credentials: `admin` / `admin`
- Edge Gateway: http://kubernetes.docker.internal/api
- Spring Boot Admin: http://kubernetes.docker.internal/admin
