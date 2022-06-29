# CS Openbanking API Mock Server

This project creates a REST server that provides a mock implementation of an API to simulate a backend system.

The API is defined by three Swagger json specs:

- [Security](docs/api-specs/Authenticate.yml) : Describes security endpoints such as User Authentication.
- [Accounts](docs/api-specs/account-info-openapi.yml) : Describes endpoints related to Accounts and Balances (e.g. list balances)


### How to build

Simply type:
```
$ mvn clean package
```

### How to run

There are two ways you can run this Mock Server:

##### 1. As a Spring Boot application running locally

Just type the following command from the root directory of the project:

```
$ mvn spring-boot:run
```

##### 2. As a Spring Boot application running in a Docker container

Note for this option, you need to have Docker installed in your workstation. or you can use [Colima](https://github.com/abiosoft/colima) instead.

First, build the Docker image by typing the following command:

```
$ mvn compile jib:dockerBuild  
```

You can see the list of all docker images on your system using the command:

```
$ docker image ls
```

This should display our newly built docker image:

```
REPOSITORY                                 TAG                 IMAGE ID    
core-bank-api-mock-server           1.0-SNAPSHOT        277188d053b7 
```

Once you have the docker image, you can run it using `docker run` command like so:

```
$ docker run -d -p 9091:9091 core-bank-api-mock-server:1.0-SNAPSHOT
```

-d option runs the container in the background and as a result it gives you
the container ID. You can see the list of all containers running with:

```
$ docker container ls

CONTAINER ID        IMAGE                                          COMMAND                 CREATED          STATUS  
a9f84f3cd35d        core-bank-api-mock-server:1.0-SNAPSHOT  "java -jar /opt/app.…"  2 seconds ago    Up 1 second
```

### View Swagger docs

Once running, you can access the API documentation at:

- [swagger-ui](http://localhost:9091/swagger-ui.html)
- [api-docs](http://localhost:9091/api-docs)


### How to test

Do you have postman?

If so, just import [Postman Collection](postman/Mock API Server Postman.postman_collection.json).

## Implement Mock Controllers

The project provides a set of utility classes that helps to implement the mock controllers and offers an opinionated way to implemented them.

### Mock Files

The data to be returned by the mock controllers could be defined in a json file, this file has the structure:
```json
{
  "status-code": 200,
  "response": {
  }
}
```
Where `status-code` is the http response status and the `response` field is the json object to be returned by the mock server

These files should be located in any folder inside of the `mock-data` folder in the `resources` folder of the project. The files
could be prefixed with any value so it could identified faster as `error-account101`. The expected file extension for the files is `.json`.
All of these parameters could be configured in the `application.yml`

```yaml
mock-server:
  root-path: classpath:mock-data
  file-extension: .json
  prefix-separator: "-"
```
For example a mock file could be located as `resources/mock-data/accounts/error-accounts101`

### Controllers

To implement a mock controller the project provides the abstract class `AbstractMockController` that contains common logic 
to help in the implementation of the controller's method avoiding boilerplate code and duplication of code.

This could be an extract for a controller implementation:

```java
@Controller
public class OpenBankingAccountsApiController extends AbstractMockController implements OpenBankingAccountsApi {

    private static final Logger logger = LoggerFactory.getLogger(OpenBankingAccountsApiController.class);

    @Override
    protected MockDataPathConfiguration defineMockDataPathConfiguration() {
        return new MockDataPathConfiguration("accounts", "accounts");
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResponseEntity<AccountsList> getAccounts(
            @ApiParam(value = "Authorisation token provided following authentication", required = true)
            @RequestHeader(value = "X-Authorization-B2C", required = true) String xAuthorizationB2C,
            @ApiParam(value = "An RFC4122 UID - used for idempotence", required = true)
            @RequestHeader(value = "CWExternalReference", required = true) String cwExternalReference) {

        validateField(xAuthorizationB2C, "Header: X-Authorization-B2C");
        TokenDecoder decodedToken = TokenDecoder.getDecoded(xAuthorizationB2C);
        return mockDataResponse(decodedToken.CWUser);
    }
}
```

You could review the classes documentation to see more information.

### Other classes

The project provides other classes to support common mechanism between the mock controllers. 

- `MockControllerInterceptor` Implement common logic before and after a controller's method invocation
- `ResponseExceptionHandler` Implement the logic to manage Exception raise by the mock server classes
- `MockPathProcessor` Process the json mock file, if you want to change the structure of the file this class should be modified.

The idea behind all the classes provided by the project is to be updated and customized depending of the project's need.


### Exercise
Generate the Payment Initiation API Mock Controller and create mock data under /mock-data for Open Banking API Spec [Open Banking API](https://github.com/OpenBankingUK/read-write-api-specs).