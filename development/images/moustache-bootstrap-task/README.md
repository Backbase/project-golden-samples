# moustache-bootstrap-task

This image is not available in any Backbase public repository.

You have the following options:

## Pulling from Harbor internal (VPN connection required)

After connecting to Backbase VPN you can run:
```shell
docker login harbor.backbase.eu
docker pull harbor.backbase.eu/development/moustache-bootstrap-task:2.82.0
docker tag harbor.backbase.eu/development/moustache-bootstrap-task:2.82.0 moustache-bootstrap-task:latest
```

## Building the image locally

You can build it by using the following commands:

```shell
./build.sh
```
