package com.example.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.filter.TokenRelayFilterFunctions.tokenRelay;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ApiGatewayApplication {

    @Bean
    public RouterFunction<ServerResponse> gatewayRouterFunctionsLoadBalancer() {
        return route("car-service")
            .route(path("/home/**"), http())
            .filter(lb("car-service"))
            .filter(tokenRelay())
            .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}

record Car(String name) {
}

@FeignClient(name = "car-service", fallback = Fallback.class)
interface CarClient {

    @GetMapping("/cars")
    CollectionModel<Car> readCars();

}

@Component
class Fallback implements CarClient {

    @Override
    public CollectionModel<Car> readCars() {
        return CollectionModel.empty();
    }
}

@RestController
class CoolCarController {

    private final CarClient carClient;

    public CoolCarController(CarClient carClient) {
        this.carClient = carClient;
    }

    @GetMapping("/cool-cars")
    public Collection<Car> coolCars() {
        return carClient.readCars()
            .getContent()
            .stream()
            .filter(this::isCool)
            .collect(Collectors.toList());
    }

    private boolean isCool(Car car) {
        return !car.name().equals("AMC Gremlin") &&
            !car.name().equals("Triumph Stag") &&
            !car.name().equals("Ford Pinto") &&
            !car.name().equals("Yugo GV");
    }
}
