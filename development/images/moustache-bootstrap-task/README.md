# moustache-bootstrap-task

The `moustache-bootstrap-task` runs the OOTB Bootstrap Tasks using the `moustache-bank` profile to include a minimal
setup.

As this image is not available in any Backbase public repository you have the following options:

## Pulling from Harbor internal (VPN connection required)

After connecting to Backbase VPN you can run:

```shell
docker login harbor.backbase.eu
docker pull harbor.backbase.eu/development/moustache-bootstrap-task:2.82.0
docker tag harbor.backbase.eu/development/moustache-bootstrap-task:2.82.0 moustache-bootstrap-task:latest
```

## Building the image locally

You can build it by using the following command:

```shell
./build.sh
```

## Execution 

### Configuration

Default configuration for the task execution (including health checks) is defined by the following environment variables:

```properties
DBS_TOKEN_URI=http://token-converter:8080/oauth/token
ADMIN_BASEPATH=http://backbase-identity:8080/auth
BACKBASE_STREAM_IDENTITY_IDENTITYINTEGRATIONBASEURL=http://identity-integration-service:8080
BACKBASE_STREAM_DBS_ACCESSCONTROLBASEURL=http://access-control:8080
BACKBASE_STREAM_DBS_ARRANGEMENTMANAGERBASEURL=http://arrangement-manager:8080
BACKBASE_STREAM_DBS_USERMANAGERBASEURL=http://user-manager:8080
```

> The env variables format in SCREAMINGSNAKE_CASE is mandatory.
