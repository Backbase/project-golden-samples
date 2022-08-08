# Setup Backbase Local Environment (docker-compose)

In this guide we'll create a lightweight Backbase setup configuring using docker-compose file.

## Pre-requisites
- Colima runing with Docker and Docker Compose
```sh
$ brew install colima docker docker-compose docker-credential-helper
```
- Start Colima
```sh
$ colima start --cpu 4 --memory 16
```

- Backbase Repository Credentials `User your backbase credientials to login to backbase repo`
```sh
$ docker login repo.backbase.com/backbase-docker-releases
```

## Steps

Once everything is installed and the docker is up and running you can execute the following steps:
- Check all running containers
```sh
$ docker ps
```
- Check backbase docker registory
```sh
$ docker pull repo.backbase.com/backbase-docker-releases/edge:2022.07
```
- Inside docker-compose directory run to the following to start up the env.
```sh 
$ docker-componse --profile boostrap up -d
```
> The profile `boostrap` is only required during the first execution (to ingest de data into DBS), after that you can simply run:
> `docker-componse up -d`


### Useful commands
```sh
# Kill all runing containers
$ docker kill $(docker ps -q)

# Remove database volume to clean up the environment
$ docker volume rm docker-compose_dev_mysql_data

# Tail docker-compose logs
$ docker-compose logs -f 
```

### Backbase Services

- Edge
- Identity Server
    * With `backbase` realm included.
- Identity Integration
- Token Converter
- Access Control
- Arrangement Manager
- User Manager

## Endpoints

Once your environment is up and running you can access it using the following URLs:

- Employee Web App: http://localhost:8080/
    * Employee Admin Credentials: `admin` / `admin`
- Identity: http://localhost:8180/auth
    * Realm Admin Credentials: `admin` / `admin`
- Edge Gateway: http://localhost:8080/api
- Registory: http://localhost:8761
