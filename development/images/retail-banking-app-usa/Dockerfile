FROM node:16 AS builder

RUN curl -s "https://start.backbase.com/productised/product-retail-banking-mobile-web-usa?version=2023.02-LTS" -o retail-banking-app-usa-package.zip
RUN unzip retail-banking-app-usa-package.zip -d /tmp/retail-banking-app-usa-package

WORKDIR /tmp/retail-banking-app-usa-package/web/retail-usa-web

RUN npm i -g replace-in-file
RUN replace-in-file '${AUTH_URL}realms/${AUTH_REALM}' '${AUTH_URL}' apps/retail-usa/src/environments/environment.prod.ts

RUN --mount=type=secret,id=npm,dst=./.npmrc npm i --legacy-peer-deps
RUN npm run build

FROM repo.backbase.com/backbase-docker-releases/web-base:1.1.4

COPY --from=builder /tmp/retail-banking-app-usa-package/web/retail-usa-web/dist/apps/retail-usa /statics
