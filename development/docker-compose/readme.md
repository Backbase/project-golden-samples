# Setup Backbase Local Environment (docker-compose)

In this guide we'll create a lightweight Backbase setup configuring using docker-compose file.

## Pre-requisites

- Any docker runtime;

> You can use Colima running with Docker and Docker Compose:
```shell
brew install colima docker docker-compose docker-credential-helper
colima start --cpu 4 --memory 16
```

- Backbase Repository Credentials, use your backbase credentials to login to backbase repo:
```shell
docker login repo.backbase.com/backbase-docker-releases
```

## Steps

Once everything is installed and the docker is up and running you can execute the following steps:
- Check all running containers
```shell
docker ps
```
- Check backbase docker registry
```shell
docker pull repo.backbase.com/backbase-docker-releases/edge:2022.07
```
- Inside docker-compose directory run to the following to start up the env.
```shell
docker compose up -d
```

> The profile `boostrap` is required for the first execution (to ingest de data into DBS):
```shell
docker compose --profile boostrap up -d
```

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

- Legal Entity Bootstrap Task

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
