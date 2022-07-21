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
* Obtain the [employee-web-app-essentials](images/employee-web-app-essentials/README.md) image as it is not yet public
  available.
* Obtain the [moustache-bootstrap-task](images/moustache-bootstrap-task/README.md) image as it is not public available.
* Run: `helmfile sync`.

> Grab a coffee and wait for a few minutes, so everything can start. I recommend installing `k9s` to monitor the status
> of the pods.

**Hint**: You can test if the environment is up once the Job `job-moustache-bootstrap-task-retail-bootstrap-task` is completed:
```shell
$ kubectl get job job-moustache-bootstrap-task-retail-bootstrap-task

NAME                                                 COMPLETIONS   DURATION   AGE
job-moustache-bootstrap-task-retail-bootstrap-task   1/1           56s        138m
```

## Components
### Infrastructure

- MySQL
- ActiveMQ
- Nginx Ingress Controller
- Spring Boot Admin Server

### Backbase Services

- Edge
- Identity Server
  * With `backbase` realm included.
- Identity Integration
- Token Converter
- Access Control
- Arrangement Manager
- User Manager

### Jobs
- Moustache Bootstrap Task

## Endpoints

Once your environment is up and running you can access it using the following URLs:

- Employee Web Portal: http://kubernetes.docker.internal/
    * Employee Admin Credentials: `admin` / `admin`
- Identity: http://kubernetes.docker.internal/auth
    * Realm Admin Credentials: `admin` / `admin`
- Edge Gateway: http://kubernetes.docker.internal/api
- Spring Boot Admin: http://kubernetes.docker.internal/admin

### Internal Endpoints

Internal endpoints are made available via ingress in case you want to access internal APIs outside the cluster:

- Identity Integration: http://kubernetes.docker.internal/internal/identity-integration-service
- Token Converter: http://kubernetes.docker.internal/internal/token-converter
- Access Control: http://kubernetes.docker.internal/internal/access-control
- Arrangement Manager: http://kubernetes.docker.internal/internal/arrangement-manager
- User Manager: http://kubernetes.docker.internal/internal/user-manager
