package com.example.carservice;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@EnableDiscoveryClient
@SpringBootApplication
public class CarServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarServiceApplication.class, args);
	}

	@Bean
	ApplicationRunner init(CarRepository repository) {
		repository.deleteAll();
		return args -> {
			Stream.of("Ferrari", "Jaguar", "Porsche", "Lamborghini", "Bugatti",
					"AMC Gremlin", "Triumph Stag", "Ford Pinto", "Yugo GV").forEach(name -> {
				repository.save(new Car(name));
			});
			repository.findAll().forEach(System.out::println);
		};
	}
}

interface CarRepository extends JpaRepository<Car, Long> {
}

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

@Entity
class Car {

	public Car() {
	}

	public Car(String name) {
		this.name = name;
	}

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Car car = (Car) o;

		if (!Objects.equals(id, car.id)) return false;
		return name.equals(car.name);
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + name.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Car{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
