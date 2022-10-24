# Setup Backbase Local Environment (docker-compose)

In this guide we'll create a lightweight Backbase setup configuring using docker-compose file.

## Pre-requisites

- Any docker runtime;

> You can use Colima running with Docker and Docker Compose:
```shell
brew install colima docker docker-compose docker-credential-helper
colima start --cpu 4 --memory 16
```

- Backbase Repository Credentials, use your Backbase credentials to login to the Backbase repo:
```shell
docker login repo.backbase.com
```
> If connected to the VPN you can also login to harbor: `docker login harbor.backbase.eu`

## Steps

Once everything is installed and the docker is up and running you can execute the following steps:
- Check all running containers
```shell
docker ps
```
- Check backbase docker registry
```shell
docker pull repo.backbase.com/backbase-docker-releases/edge:2022.09.1
```
- Inside docker-compose directory run to the following to start up the env.
```shell
docker compose up -d
```

> The profile `boostrap` is required for the first execution (to ingest de data into DBS):
```shell
docker compose --profile=bootstrap up -d
```

> **Heads up**: The image `harbor.backbase.eu/development/employee-web-app-essentials` is not publicly available, you can [build it](../images/employee-web-app-essentials/README.md) or pull it from Harbor.

### Useful commands
- Check logs:
```shell
docker compose logs -f
```

- Kill all running containers described in the docker compose file:
```shell
docker compose down
```

- Kill all running containers in the host:
```shell
docker kill $(docker ps -q)
```

### Backbase Services

- Edge
- Registry
- Identity Server
    * With `backbase` realm included.
- Identity Integration
- Token Converter
- Access Control
- Arrangement Manager
- User Manager

### Jobs

- Product Catalog Task
- Legal Entity Bootstrap Task
> With `moustache-bank` and `moustache-bank-subsidiaries` profiles enabled. They are [pre-configured]((https://github.com/Backbase/stream-services/blob/master/stream-legal-entity/legal-entity-bootstrap-task/src/main/resources/application.yml#L24)) in the Stream services for demonstration purposes.

### Web Applications

- [Employee Web App Essentials](https://community.backbase.com/documentation/employee_web_app/latest/deploy_web_app)


## Endpoints

Once your environment is up and running you can access it using the following URLs:

- Employee Web App: http://localhost:8080
    * Employee Admin Credentials: `admin` / `admin`
- Identity: http://localhost:8180/auth
    * Realm Admin Credentials: `admin` / `admin`
- Edge Gateway: http://localhost:8080/api
- Registry: http://localhost:8761
