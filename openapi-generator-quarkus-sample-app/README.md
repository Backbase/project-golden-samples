# OpenApi Generator Usage on Sample Quarkus Application

This example covers the approach adopted on the Natwest Identity project when generating API clients with Resteasy for
**Identity Service Provider Interfaces** which they are using Quarkus native libraries.

For **Spring**, with the help of the **boat-plugin**, we have the default configuration to use open-api-generator with
Spring. It's already documented and is well known in Backbase.

Also, multi-tenancy (while creating api-client) and aspects of tracing will be covered with the examples.

> **_NOTE:_** Please take a look at
> the [BOAT Golden Example](https://github.com/Backbase/project-golden-samples/tree/main/boat)
> to understand how to use boat-plugin.

For **Quarkus**, to use openapi-generator the plugin must be configured.

## Configuration of OpenApiGenerator Plugin

The input spec of this sample application can be
found [here](https://raw.githubusercontent.com/redhat-appdev-practice/todo-api/trunk/openapi.yml).

```xml

<plugin>
    <groupId>org.openapitools</groupId>
    <artifactId>openapi-generator-maven-plugin</artifactId>
    <executions>
        <execution>
            <id>todo-api</id>
            <goals>
                <goal>generate</goal>
            </goals>
            <configuration>
                <inputSpec>${project.basedir}/src/main/resources/todo.yaml</inputSpec>
                <configOptions>
                    <modelPackage>org.quarkus.openapi.todo.model</modelPackage>
                    <apiPackage>org.quarkus.openapi.todo.api</apiPackage>
                </configOptions>
            </configuration>
        </execution>
    </executions>
    <configuration>
        <output>${codegen.openapi.generated-sources-dir}</output>
        <generatorName>java</generatorName>
        <generateApiTests>false</generateApiTests>
        <generateModelTests>false</generateModelTests>
        <generateModelDocumentation>false</generateModelDocumentation>
        <generateApiDocumentation>false</generateApiDocumentation>
        <configOptions>
            <library>resteasy</library>
            <dateLibrary>java8</dateLibrary>
            <openApiNullable>false</openApiNullable>
            <invokerPackage>org.quarkus.openapi.todo.api</invokerPackage>
        </configOptions>
    </configuration>
</plugin>
```

```<library>resteasy</library>```

The resteasy library is used to generate resteasy client.

```<dateLibrary>java8</dateLibrary>```

Java8 date library is used.

```<openApiNullable>false</openApiNullable>```

OpenAPI Jackson Nullable library is disabled.

```<invokerPackage>org.quarkus.openapi.todo.api</invokerPackage>```

Root package for generated code
## Tracing
```TODO : Tracing will be covered```

## Multi-tenancy

Each time an API request is performed, we need to know to tenant identifier to correctly route the persistence
operations.

There are around three most common ways to provide tenant identifier.

1. Providing the tenant identifier as a **URL Part**
2. Using a custom **HTTP Request header**
3. Using JWTs to provide the tenant identifier as a JSON token claim

For this sample-app, second option through a custom HTTP header called **'X-TID'** is used.


```java
    public class ApiClientFactory {

    private ApiClientFactory() {
    }

    /**
     * Getting ApiClient configured for given tenant.
     */
    public static ApiClient createTraceApiClient(String basePath) {
        ApiClient apiClient = new ApiClient().setBasePath(basePath);
        Tracer tracer = GlobalTracer.get();
        ClientBuilder clientBuilder = ClientBuilder.newBuilder()
            .executorService(new TracedExecutorService(Executors.newCachedThreadPool(), tracer))
            .register(new SmallRyeClientTracingFeature(tracer))
            .register(apiClient.getJSON());

        if (LoggerFactory.getLogger(ApiClient.class).isDebugEnabled()) {
            clientBuilder.register(Logger.class);
        }

        return apiClient.setHttpClient(clientBuilder.build());
    }

    /**
     * Getting ApiClient configured for given tenant (or without tenant if tenantId is null).
     */
    public static ApiClient createTenantApiClient(String tenantId, String basePath) {
        ApiClient apiClient = createTraceApiClient(basePath);
        if (Objects.isNull(tenantId)) {
            return apiClient;
        }

        return apiClient.addDefaultHeader("X-TID", tenantId);
    }
}
```

After the trace api client is created, the custom **HTTP request header** (**X-TID**) is added. 

## Tests


## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/code-with-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Provided Code

### RESTEasy Reactive

Easily start your Reactive RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
