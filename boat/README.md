# BOAT Golden Example

This example consists of three modules:

- Server: This service provides a greeting message based on the received name
- Client: This service registers a user and calls the `Server` service to get a greeting message
- Api: The OpenAPI specification for the greeting services are stored in this module.

**Note**: It's important to keep your specifications in a separate module to prevent duplicating it in the server and
client.

## API

This module will package the OpenAPI specs using `maven-assembly-plugin`. Take a look at the `/api/assembly/api.xml`
file to see how it's configured.<br/>
For validating and bundling the spec file we can use `boat-maven-plugin`. Take a look at the executions of the plugin.

## Server

The server uses the api dependency to generate the rest controller interfaces.</br>
First we need to unpack the API dependency using the `maven-dependency-plugin`:

```xml

<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <executions>
        <execution>
            <id>unpack</id>
            <phase>generate-sources</phase>
            <goals>
                <goal>unpack</goal>
            </goals>
            <configuration>
                <artifactItems>
                    <artifactItem>
                        <groupId>com.backbase</groupId>
                        <artifactId>boat-example-api</artifactId>
                        <version>1.0.0</version>
                        <classifier>api</classifier>
                        <outputDirectory>${project.build.directory}/yaml</outputDirectory>
                        <type>zip</type>
                        <overWrite>true</overWrite>
                    </artifactItem>
                </artifactItems>
                <includes>**/*.yaml, **/*.json</includes>
            </configuration>
        </execution>
    </executions>
</plugin>
```

Then we can generate required DTOs and interfaces using `boat-maven-plugin`:

```xml

<plugin>
    <groupId>com.backbase.oss</groupId>
    <artifactId>boat-maven-plugin</artifactId>
    <version>${boat-maven-plugin.version}</version>
    <executions>
        <execution>
            <id>generate-client-api-code</id>
            <goals>
                <goal>generate-spring-boot-embedded</goal>
            </goals>
            <phase>generate-sources</phase>
            <configuration>
                <inputSpec>${project.build.directory}/yaml/boat-example-api/greeting-api-v1.0.0.yaml
                </inputSpec>
                <apiPackage>com.backbase.greeting.api.service.v1</apiPackage>
                <modelPackage>com.backbase.greeting.api.service.v1.model</modelPackage>
                <typeMappings>
                    <typeMapping>OffsetDateTime=java.time.ZonedDateTime</typeMapping>
                </typeMappings>
            </configuration>
        </execution>
    </executions>
</plugin>
```

Notice the `<goal>generate-spring-boot-embedded</goal>` which generates required files for server implementation.

## Client

The client also uses the API dependency to generate required DTOs and client files.<br/>
First we need to unpack the API dependency using the `maven-dependency-plugin` like the server config.<br/>
Then we can generate required DTOs and client using `boat-maven-plugin`:

```xml

<plugin>
    <groupId>com.backbase.oss</groupId>
    <artifactId>boat-maven-plugin</artifactId>
    <version>${boat-maven-plugin.version}</version>
    <executions>
        <execution>
            <id>boat-example-api</id>
            <phase>generate-sources</phase>
            <goals>
                <goal>generate-rest-template-embedded</goal>
            </goals>
            <configuration>
                <inputSpec>${project.build.directory}/yaml/boat-example-api/greeting-api-v1.0.0.yaml
                </inputSpec>
                <apiPackage>com.backbase.greeting.api.service.v1</apiPackage>
                <modelPackage>com.backbase.greeting.api.service.v1.model</modelPackage>
                <typeMappings>
                    <typeMapping>OffsetDateTime=java.time.ZonedDateTime</typeMapping>
                </typeMappings>
            </configuration>
        </execution>
    </executions>
</plugin>
```

Notice the `<goal>generate-rest-template-embedded</goal>` which generates required files for client implementation.

### Client config

We need to create and configure api beans:

```java

@Configuration
public class GreetingClientConfiguration {
    @Value("${backbase.example.greeting-base-url}")
    private String greetingBasePath;

    @Bean
    public ApiClient greetingApiClient() {
        ApiClient apiClient = new ApiClient(new RestTemplate());
        apiClient.setBasePath(this.greetingBasePath);
        apiClient.addDefaultHeader(HttpCommunicationConfiguration.INTERCEPTORS_ENABLED_HEADER, Boolean.TRUE.toString());
        return apiClient;
    }

    @Bean
    public GreetingApi createConfirmationApi(@Qualifier("greetingApiClient") ApiClient greetingApiClient) {
        return new GreetingApi(greetingApiClient);
    }
}
```

In this example for simplicity we haven't used token converter and made all the endpoints public and they can be called
without providing any tokens.<br/>
If we want to call a service-api, we need to provide a client-credential token which is created by the Token Converter
service.<br/>
We can use the rest template that is provided by the SSDK communication library which will automatically injects the
client credential token in the request:

```java
    @Bean
public ApiClient greetingApiClient(@Qualifier(INTER_SERVICE_REST_TEMPLATE_BEAN_NAME) RestTemplate restTemplate){
        ApiClient apiClient=new ApiClient(restTemplate);
        apiClient.setBasePath(this.greetingBasePath);
        apiClient.addDefaultHeader(HttpCommunicationConfiguration.INTERCEPTORS_ENABLED_HEADER,Boolean.TRUE.toString());
        return apiClient;
        }
```

The `INTERCEPTORS_ENABLED_HEADER` header enables the `ApiErrorExceptionInterceptor` class of the SSDK communication
library to intercept errors.

### Enabling logging

We can enable request and response logging in api client using debug option:

```java
    @Bean
public ApiClient greetingApiClient(){
        ApiClient apiClient=new ApiClient(new RestTemplate());
        apiClient.setBasePath(this.greetingBasePath);
        apiClient.addDefaultHeader(HttpCommunicationConfiguration.INTERCEPTORS_ENABLED_HEADER,Boolean.TRUE.toString());
        apiClient.setDebugging(true);
        return apiClient;
        }
```

For more info you can read
the [boat documentation](https://github.com/Backbase/backbase-openapi-tools/blob/main/boat-maven-plugin/README.md)