package com.example.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.stream.StreamSupport;

@EnableDiscoveryClient
@SpringBootApplication
public class ApiGatewayApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiGatewayApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    // @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        final Environment env = event.getApplicationContext()
            .getEnvironment();

        LOGGER.info("Active profiles: {}", Arrays.toString(env.getActiveProfiles()));

        final MutablePropertySources sources = ((AbstractEnvironment) env).getPropertySources();

        StreamSupport.stream(sources.spliterator(), false)
            .filter(ps -> ps instanceof EnumerablePropertySource)
            .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
            .flatMap(Arrays::stream)
            .distinct()
            .filter(prop -> !(prop.contains("credentials") || prop.contains("password")))
            .forEach(prop -> LOGGER.info("{}: {}", prop, env.getProperty(prop)));
    }
}


record Car(String name) {
}

@RestController
class CoolCarController {

    Logger log = LoggerFactory.getLogger(CoolCarController.class);

    private final WebClient.Builder webClientBuilder;
    private final ReactiveCircuitBreaker circuitBreaker;

    public CoolCarController(WebClient.Builder webClientBuilder, ReactiveCircuitBreakerFactory circuitBreakerFactory) {
        this.webClientBuilder = webClientBuilder;
        this.circuitBreaker = circuitBreakerFactory.create("circuit-breaker");
    }


    @GetMapping("/cool-cars")
    public Flux<Car> goodCars() {
        return circuitBreaker.run(
                webClientBuilder.build()
                    .get().uri("http://car-service/cars")
                    .retrieve().bodyToFlux(Car.class)
                    .filter(this::isCool),
            throwable -> {
                log.warn("Error making request to car service", throwable);
                return Flux.empty();
            });
    }

    private boolean isCool(Car car) {
        return !car.name().equals("AMC Gremlin") &&
            !car.name().equals("Triumph Stag") &&
            !car.name().equals("Ford Pinto") &&
            !car.name().equals("Yugo GV");
    }
}

@RestController
class HomeController {

    @GetMapping("/")
    public String howdy(@AuthenticationPrincipal OidcUser user) {
        return "Hello, " + user.getFullName();
    }


    @GetMapping("/print-token")
    public String printAccessToken(@RegisteredOAuth2AuthorizedClient("okta")
                                   OAuth2AuthorizedClient authorizedClient) {

        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

        System.out.println("Access Token Value: " + accessToken.getTokenValue());
        System.out.println("Token Type: " + accessToken.getTokenType().getValue());
        System.out.println("Expires At: " + accessToken.getExpiresAt());

        return "Access token printed";
    }
}
