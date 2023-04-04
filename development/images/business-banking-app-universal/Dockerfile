FROM node:16 AS builder

RUN curl -s "https://start.backbase.com/productised/product-business-banking-mobile-universal?version=2023.02-LTS" -o business-banking-app-universal-package.zip
RUN unzip business-banking-app-universal-package.zip -d /tmp/business-banking-app-universal-package

WORKDIR /tmp/business-banking-app-universal-package/web/business-banking-universal-app

RUN npm i -g replace-in-file
RUN --mount=type=secret,id=npm,dst=./.npmrc npm i --legacy-peer-deps

RUN replace-in-file '${AUTH_URL}realms/${AUTH_REALM}' '${AUTH_URL}' apps/business-universal/src/environments/environment.prod.ts
RUN npm run build

FROM repo.backbase.com/backbase-docker-releases/web-base:1.1.4

COPY --from=builder /tmp/business-banking-app-universal-package/web/business-banking-universal-app/dist/apps/business-universal /statics
