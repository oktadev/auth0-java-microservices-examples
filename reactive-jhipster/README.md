# Reactive Java Microservices with Spring Boot and JHipster 🤓

I presented this example as part of The Golden Path to SpringOne 2023.

- 📺 [Recording on YouTube](https://www.youtube.com/watch?v=ECZvGkzOsbQ)
- 📚 [Presentation on Speaker Deck](https://speakerdeck.com/mraible/reactive-microservices-with-spring-boot-and-jhipster-the-golden-path-to-springone-2023)
- ⛑️ [Demo script](demo.adoc)

This example uses JHipster 8 to generate a reactive microservices architecture with Spring Cloud Gateway and Spring WebFlux. See [Reactive Java Microservices with Spring Boot and JHipster](demo.adoc) to see how it was built.

**Prerequisites:** [Java 17](https://sdkman.io/sdks#java), [Node 18](https://nodejs.org/en/), and [Docker](https://docs.docker.com/engine/install/).

* [Getting Started](#getting-started)
* [Links](#links)
* [Help](#help)
* [License](#license)

## Getting Started

To install this example, run the following commands:

```bash
git clone https://github.com/oktadev/auth0-java-microservices-examples.git
cd auth0-java-microservices-examples/reactive-jhipster
```

The JHipster Registry and Spring Cloud Config are pre-configured to use Auth0. That means if you try to run them, you won't be able to login until you create an account, and an application in it.

Install the Auth0 CLI using the instructions on [github.com/auth0/auth0-cli](https://github.com/auth0/auth0-cli) and come back here when you're done. If you don't have an Auth0 account, signup at <https://auth0.com/signup>.

**NOTE**: You can also use your browser and Auth0's dashboard to register an app. See [JHipster's security documentation](https://www.jhipster.tech/security/#auth0) for those instructions.

In the gateway project's directory, run the command below. Accept the default redirect URIs.

```shell
auth0 apps create
```

Use the following values when prompted:

- Name: `Reactive Stack`
- Type: `Regular Web Application`
- Callback URLs: `http://localhost:8080/login/oauth2/code/oidc,http://localhost:8761/login/oauth2/code/oidc`
- Allowed Logout URLs: `http://localhost:8080,http://localhost:8761`

In the [roles](https://manage.auth0.com/#/roles) section or your Auth0 dashboard, create new roles named `ROLE_ADMIN` and `ROLE_USER`.

Create a new user account in the [users](https://manage.auth0.com/#/users) section. Click the **Role** tab to assign the roles you just created to the new account.

_Make sure your new user's email is verified before attempting to log in!_

Next, head to **Actions** > **Flows** and select **Login**. Create a new action named `Add Roles` and use the default trigger and runtime. Change the `onExecutePostLogin` handler to be as follows:

```js
exports.onExecutePostLogin = async (event, api) => {
  const namespace = 'https://www.jhipster.tech';
  if (event.authorization) {
    api.idToken.setCustomClaim('preferred_username', event.user.email);
    api.idToken.setCustomClaim(`${namespace}/roles`, event.authorization.roles);
    api.accessToken.setCustomClaim(`${namespace}/roles`, event.authorization.roles);
  }
}
```

Select **Deploy** and drag the `Add Roles` action to your Login flow.

Spring Cloud Config allows you to distribute Spring's configuration between apps. Update `gateway/src/main/docker/central-server-config/localhost-config/application.yml` to use your Auth0 app settings. 

```yaml
jhipster:
  ...
  security:
    oauth2:
      audience:
        - https://<your-auth0-domain>/api/v2/

spring:
  security:
    oauth2:
      client:
        provider:
          oidc:
            issuer-uri: https://<your-auth0-domain>/
        registration:
          oidc:
            client-id: <client-id>
            client-secret: <client-secret>
```

Save your changes. These values will be distributed to the JHipster Registry, gateway, blog, and store apps. Start all the services and apps using the following commands:

```shell
cd gateway
docker-compose -f src/main/docker/keycloak.yml up -d #jhkeycloakup
docker-compose -f src/main/docker/postgresql.yml up -d #jhpostgresqlup
docker-compose -f src/main/docker/jhipster-registry.yml up -d #jhregistryup
./gradlew
```

Open a new terminal window, start the blog app's Neo4j database, and then the app itself.

```shell
cd ../blog
docker-compose -f src/main/docker/neo4j.yml up -d #jhneo4jup
./gradlew
```

Then, open another terminal window, start the store app's MongoDB database, and the microservice.

```shell
cd ../store
docker-compose -f src/main/docker/mongodb.yml up -d #jhmongoup
./gradlew
```

Now, open a new incognito browser window, go to `http://localhost:8080`, and sign in. Rejoice that using Auth0 for authentication works!

**TIP**: You can also run everything using Docker Compose. See the [demo script](https://github.com/oktadev/auth0-java-microservices-examples/blob/main/reactive-jhipster/demo.adoc#run-your-microservices-stack-with-docker-compose) for how to do that.

## Links

This examples uses the following open source libraries:

* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring Cloud](https://spring.io/projects/spring-cloud)
* [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)
* [Spring Security](https://spring.io/projects/spring-security)
* [JHipster](https://www.jhipster.tech)
* [OpenJDK](https://openjdk.java.net/)

## Help

Please post any questions as issues in this repository, or ask them on Stack Overflow with the "jhipster" tag. 

## License

Apache 2.0, see [LICENSE](LICENSE).
