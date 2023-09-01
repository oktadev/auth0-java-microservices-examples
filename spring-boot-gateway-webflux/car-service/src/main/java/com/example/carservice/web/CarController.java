package com.example.carservice.web;

import com.example.carservice.data.Car;
import com.example.carservice.data.CarRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class CarController {

    private final CarRepository repository;

    public CarController(CarRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/cars")
    public List<Car> getCars() {
        return repository.findAll();
    }
}
