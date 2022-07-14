FROM node:16 AS builder

RUN curl -s "https://start.backbase.com/productised/product-employee-essentials-web?version=2022.06" -o employee-essentials-package.zip
RUN unzip employee-essentials-package.zip -d /tmp/employee-essentials-package

WORKDIR /tmp/employee-essentials-package/web/employee-essentials

RUN --mount=type=secret,id=npm,dst=./.npmrc npm i --force
RUN npm run build:employee-essentials

FROM repo.backbase.com/backbase-docker-releases/web-base:1.1.1

COPY --from=builder /tmp/employee-essentials-package/web/employee-essentials/dist/employee-essentials /statics
