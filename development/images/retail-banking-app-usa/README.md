# Employee Essentials Web

This image is not available in any Backbase public repository. 

You have the following options:

## Pulling from Harbor internal (VPN connection required)

After connecting to Backbase VPN you can run:
```shell
docker login harbor.backbase.eu
docker pull --platform=linux/amd64 harbor.backbase.eu/development/retail-banking-app-usa:2023.02-LTS
```

## Building the image locally

You can build it by using the following commands:

```shell
# Create a .npmrc file using your credentials.
curl -s -u"<username>:<password>" https://repo.backbase.com/api/npm/npm-backbase/auth/backbase > .npmrc

# Build the image using the created .npmrc credentials as a build secret.
docker build --platform=linux/amd64 --secret id=npm,src=$(pwd)/.npmrc -t harbor.backbase.eu/development/retail-banking-app-usa:2023.02-LTS .
```

If you already have Backbase registry configured in your laptop installation you can reuse your `.npmrc` file instead of generating a new one:
```shell
# Build the image using your existing credentials as a build secret.
docker build --platform=linux/amd64 --secret id=npm,src=$(echo $HOME)/.npmrc -t harbor.backbase.eu/development/retail-banking-app-usa:2023.02-LTS .
```

> Mounting the secret as a `.npmrc` file is necessary to fetch the NPM dependencies on Backbase private registry.
