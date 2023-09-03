# Java Microservices with Spring Boot and Spring Cloud

This example is a bare-bones microservices architecture built with Spring Boot, Spring Cloud, Spring Cloud Gateway, and WebClient.

**Prerequisites:** [Java 17](https://sdkman.io/sdks#java) and [Docker](https://docs.docker.com/engine/install/).

* [Getting Started](#getting-started)
* [Links](#links)
* [Help](#help)
* [License](#license)

## Getting Started

To install this example, run the following commands:

```bash
git clone https://github.com/oktadev/auth0-java-microservices-examples.git
cd auth0-java-microservices-examples/spring-boot-gateway-webflux
```

The `api-gateway` and `car-service` projects are already pre-configured to be locked down with OAuth 2.0 and Auth0. That means if you try to run them, you won't be able to log in until you create an account, and an application in it.

[Install the Auth0 CLI](https://github.com/auth0/auth0-cli) and come back here when you're done. If you don't have an Auth0 account, [sign up for free](https://auth0.com/signup).

In the root project's directory, run the command below to register an OIDC app with Auth0.

```shell
auth0 apps create \
  --name "Kick-Ass Cars" \
  --description "Microservices for Cool Cars" \
  --type regular \
  --callbacks http://localhost:8080/login/oauth2/code/okta \
  --logout-urls http://localhost:8080 \
  --reveal-secrets
```

Copy the issuer, client ID, client secret, and audience into a `.env` file in the `api-gateway` directory.

```dotenv
OKTA_OAUTH2_ISSUER=https://<auth0-domain>/
OKTA_OAUTH2_CLIENT_ID=<auth0-client-id>
OKTA_OAUTH2_CLIENT_SECRET=<auth0-client-secret>
OKTA_OAUTH2_AUDIENCE=https://<auth0-domain>/api/v2/
```

Start the Eureka server in one terminal window for service discovery:

```shell
cd discovery-service
./gradlew bootRun
```

Copy the issuer and audience into `car-service/.env`:

```dotenv
OKTA_OAUTH2_ISSUER=https://<auth0-domain>/
OKTA_OAUTH2_AUDIENCE=https://<auth0-domain>/api/v2/
```

Open another terminal window and start the car microservice:

```shell
cd car-service
./gradlew bootRun
```

Then, open a third terminal window and start the API gateway:

```shell
cd api-gateway
./gradlew bootRun
```

Make sure all your apps have started and registered by looking at the Eureka dashboard at `http://localhost:8761`.

Now, open a new incognito browser window, go to `http://localhost:8080`, and sign in. Rejoice that using Auth0 for authentication works!

You can also test out the following features:

- `http://localhost:8080/cool-cars`: Gets a list of cars from the car service with `WebClient`. If you shut down the car service, Spring Cloud Circuit Breaker will return a fallback response. Start the car service, and data will start flowing again.
- `http://localhost:8080/home`: Proxies the request to `http://car-service/home` with Spring Cloud Gateway and its `TokenRelayFilter`.

You can add refresh token support by adjusting `api-gateway/.env` to have:

```
OKTA_OAUTH2_SCOPES=openid,profile,email,offline_access
OKTA_OAUTH2_AUDIENCE=http://fast-expiring-api
```

Then, create an API in Auth0 called `fast-expiring-api` and set the TTL to 30 seconds.
 
<!-- todo: add instructions for creating an API with the Auth0 CLI -->

Restart the API gateway. 

```java
cd api-gateway
./gradlew bootRun
```

Then, go to `http://localhost:8080/print-token` to see your access token. You can copy the expired time to [timestamp-converter.com](https://www.timestamp-converter.com/) to see when it expires in your local timezone. Wait 30 seconds and refresh the page. You'll see a request to get a new token and an updated expires timestamp in your terminal.

## Links

This examples uses the following open source libraries:

* [Okta Spring Boot Starter](https://github.com/okta/okta-spring-boot)
* [SDKMAN](https://sdkman.io/)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring Cloud](https://spring.io/projects/spring-cloud)
* [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)
* [Spring Security](https://spring.io/projects/spring-security)

## Help

Please post any questions as issues in this repository, or ask them in the [Auth0 Community](https://community.auth0.com/).

## License

Apache 2.0, see [LICENSE](LICENSE).
