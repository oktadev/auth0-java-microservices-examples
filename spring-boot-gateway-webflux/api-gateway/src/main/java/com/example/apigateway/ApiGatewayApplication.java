package com.example.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}

record Car(String name) {
}

@FeignClient("car-service")
interface CarClient {

    @GetMapping("/cars")
    @CrossOrigin
    CollectionModel<Car> readCars();
}

@RestController
class CoolCarController {

    private final CarClient carClient;

    public CoolCarController(CarClient carClient) {
        this.carClient = carClient;
    }

    @GetMapping("/cool-cars")
    @CrossOrigin
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
