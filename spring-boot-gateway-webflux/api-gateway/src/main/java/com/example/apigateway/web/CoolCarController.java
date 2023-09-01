package com.example.apigateway.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
class CoolCarController {

    Logger log = LoggerFactory.getLogger(CoolCarController.class);

    private final WebClient.Builder webClientBuilder;
    private final ReactiveCircuitBreaker circuitBreaker;

    public CoolCarController(WebClient.Builder webClientBuilder,
                             ReactiveCircuitBreakerFactory circuitBreakerFactory) {
        this.webClientBuilder = webClientBuilder;
        this.circuitBreaker = circuitBreakerFactory.create("circuit-breaker");
    }

    record Car(String name) {
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
