# rest-api-server-examples

This is a sample of a very basic merchant server that can be used from the Payment Mobile SDK.

It implements all the needed operations in order to create Payment sessions and verify the integrity of the server response.
 
## Requirements

Java 7 or higher is required.

## Table of contents

* [Getting started](#getting-started)
* [How it works](#how-it-works)
* [What's included](#whats-included)
* [Security concerns](#security-concerns)
* [Configuration](#configuration)

## Getting started 

This sample application provides a REST API using [Spring Boot](https://spring.io/projects/spring-boot), so it is very easy to make it work as standalone server.  

* Clone the repo: `git clone https://github.com/lyra/rest-api-server-examples.git`.
* Set your account data in the _client-configuration.properties_ file as described in [configuration instructions](#configuration).

#### Local deployment 

* Run `mvn package`, to build jar executable
* Run `java -jar target/merchant-server-java-sample-1.0.0-SNAPSHOT.jar`

The application should start and have an output similar to this: 

    [...]
    INFO 2292 --- [main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.22]
    INFO 2292 --- [main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
    INFO 2292 --- [main] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 2485 ms
    INFO 2292 --- [main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
    INFO 2292 --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
    INFO 2292 --- [main] com.lyra.sdk.server.ServerApplication    : Started ServerApplication in 3.978 seconds (JVM running for 4.612)

By default the application run on 8080 port. See [configuration instructions](#configuration) if you want to change this value.

#### Deploy using Heroku

* Create an [Heroku account](https://signup.heroku.com/)
* Deploy using the [Heroku CLI]()
    * _heroku create_
    * _git push heroku master_
    * _heroku open_
* Alternatively you can easily deploy using this button:

[![Deploy to Heroku](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)

## How it works

This sample of merchant server provides the entry points needed to perform the implementation of Payment SDK Server.

**/createPayment** allows the creation of the payment session and returns a token that should be provided to SDK.

**/createToken allows** the creation of the payment session with a saved card and returns a token that should be provided to SDK.

**/verifyResult** checks the response of the payment server and verifies its integrity.

More info in the integration guide of mobile SDK.

## What's included

```
|---com.lyra.sdk.server
|   |-- ServerApplication.java
|   |-- resource 
|        |-- CreateResource.java
|        |-- HealthResource.java
|        |-- VerifyResultResource.java   
|   |-- util
|        |-- ServerConfiguration.java
|---resources
|   |-- application.properties
```

**ServerApplication.java**: is the entry point of the Spring Boot application.

**Resource package**: contains all the Rest controllers that handle the Rest operations.

**Util package**: contains a configuration implementation that reads data from environment variables if provided.  

## Security concerns

This sample uses a [basic auth](https://developer.mozilla.org/fr/docs/Web/HTTP/Authentication#Sch%C3%A9ma_d'authentification_basique_(Basic)) implementation in order to provide a simple way to secure the Rest API.

See [Configuration](#security-configuration) section if you want to how to configure credentials.  

Please note that this authentication requires an HTTP**S** connection in order to be really effective. Otherwise the credentials will 
be sent in plain text.
 

## Configuration

Server port can be easily configured via the standard Sprint Boot configuration file (application.properties)
 
```
server.port=8080
```

### Account Configuration

The account configuration can be set easily using either of these two mechanisms: 

#### Using configuration file

You just have to rename the file _client-configuration.properties.example_ to _client-configuration.properties_ 
and set the needed variables.

```
#Merchant account parameters
username=<REPLACE_ME>
password=<REPLACE_ME>
apiRestServerName=<REPLACE_ME>
hashKey=<REPLACE_ME>

#Connection parameters
proxyHost=
proxyPort=
connectionTimeout=45000
requestTimeout=45000
```

Your merchant account parameters are provided in your merchant BackOffice. In Settings menu -> Shop, Tab "REST API keys".  Please check your integration documentation for more information.

Note: this method should be preferred when the proxy and timeouts must be configured, since it is not currently possible 
to do it using environment variables (unless you implement it modifying ServerConfiguration class).

#### Using environment variables

You can set your account data using this environment variables.

    SDK_SERVER_USERNAME
    SDK_SERVER_PASSWORD
    SDK_SERVER_NAME
    SDK_HASH_KEY
    
    
### Security Configuration

In order to perform Basic Auth, it is necessary to provide an username and a password.
 
Both parameters can be easily defined using the standard Spring Boot mechanism.

Just modify the right keys in _application.properties_ file: 

    spring.security.user.name=user
    spring.security.user.password=CHANGEME
    
The password can be overriden using the environment variables called _SERVER_AUTH_USER_ and _SERVER_AUTH_TOKEN_