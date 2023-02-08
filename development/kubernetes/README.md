# Setup Backbase Local Environment

In this guide we'll create a lightweight Backbase setup configuring a [k3s](https://k3s.io/) cluster.

## Pre-requisites

- Colima with Kubernetes (k3s embedded by default) configured with at least **16GB of RAM** and **4 CPUs** - Or any local Kubernetes cluster you are comfortable with.
    * [Set up on MacOS](https://backbase.atlassian.net/wiki/spaces/CE/pages/3584589953/How+to+replace+Docker+Desktop+with+Colima)
    * `colima start --cpu 4 --memory 16 --kubernetes --kubernetes-disable=traefik`
> **Disclaimer**: If you already have `colima` running with insufficient resources it is recommended to delete it first
> using: `colima delete`
- Helmfile
    * `brew install helmfile`
- kubectl (and k9s)
    * `brew install kubectl k9s`
- Backbase Repository Credentials

## Steps

Once everything is installed and the cluster is up and running you can execute the following steps:

* Run `setup-backbase-credentials.sh` to set up helm and images repositories.
* Run `echo '127.0.0.1 kubernetes.docker.internal' | sudo tee -a /etc/hosts` to enable the ingress to expose the internal endpoints.
* Obtain the [harbor.backbase.eu/development/employee-web-app-essentials](../images/employee-web-app-essentials/README.md) image as it is not yet public
  available.
* Run: `helmfile sync`.

> Grab a coffee and wait for a few minutes, so everything can start. I recommend installing [k9s](https://k9scli.io/) to
> monitor the status
> of the pods.

**Hint**: You can test if the entire environment is up once the Job `stream-legal-entity-boostrap-stream-bootstrap-task`
is completed:

```shell
$ kubectl get job stream-legal-entity-boostrap-stream-bootstrap-task

NAME                                                 COMPLETIONS   DURATION   AGE
stream-legal-entity-boostrap-stream-bootstrap-task   1/1           56s        138m
```

## Components Installed

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

- Stream Legal Entity Bootstrap
  * With `moustache-bank` and `moustache-bank-subsidiaries` profiles enabled. They are [pre-configured](https://github.com/Backbase/stream-services/blob/master/stream-legal-entity/legal-entity-bootstrap-task/src/main/resources/application.yml#L24) in the Stream services for demonstration purposes.

### Web Applications

- [Employee Web App Essentials](https://community.backbase.com/documentation/employee_web_app/latest/deploy_web_app)

## Endpoints

Once your environment is up and running you can access it using the following URLs:

- Employee Web App: http://kubernetes.docker.internal/
    * Employee Admin Credentials: `admin` / `admin`
- Identity: http://kubernetes.docker.internal/auth
    * Realm Admin Credentials: `admin` / `admin`
- Edge Gateway: http://kubernetes.docker.internal/api
- Spring Boot Admin: http://kubernetes.docker.internal/admin
- Jaeger: http://kubernetes.docker.internal/jaeger

## Adding a Custom Service

You can add your custom integration service to the cluster by adding a new `backbase-app` chart deployment in the helmfile. e.g.
```yaml
  - name: dis-custom-integration-service
    chart: backbase-charts/backbase-app
    version: 0.24.1
    labels:
      tier: dis
      component: custom-integration-service
    values:
      - global.yaml.gotmpl
      - global-ssdk.yaml.gotmpl
      - app:
          name: custom-integration-service
          image:
            registry: harbor.backbase.eu/development
            repository: custom-integration-service
            tag: latest
        service:
          nameOverride: custom-integration-service
        ingress:
          enabled: true
          annotations:
            "kubernetes.io/ingress.class": "nginx"
            "nginx.ingress.kubernetes.io/rewrite-target": /$1
            "nginx.ingress.kubernetes.io/x-forwarded-prefix": "/internal/custom-integration-service"
          hosts:
            - host: {{ .Values.ingress.host }}
              paths:
                - /internal/custom-integration-service/(.*)
```

## Future Improvements

1. Use all helm charts
   from [Topstack](https://backbase.atlassian.net/wiki/spaces/BAAS/pages/3842146431/New+helm+charts+based+on+library+first+implementation)
   once it is stable.
2. Create an umbrella chart where we could replace this `helmfile` itself by a simple `helm` execution.
