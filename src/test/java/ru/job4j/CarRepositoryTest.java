package ru.job4j;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.CarRepository;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.EngineRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CarRepositoryTest {

    private CarRepository carRepository;
    private EngineRepository engineRepository;
    private Car testCar;

    private Engine testEngine;

    @BeforeEach
    public void setUp() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory();
        CrudRepository crudRepository = new CrudRepository(sf);
        carRepository = new CarRepository(crudRepository);
        engineRepository = new EngineRepository(crudRepository);


        testCar = new Car();
        testCar.setName("Test Car");
        testEngine = new Engine();
        testEngine.setName("Test Engine");
    }

    @Test
    public void whenSaveThenFindById() {
        carRepository.save(testCar);
        Optional<Car> foundCar = carRepository.findById(testCar.getId());
        assertThat(foundCar).isPresent();
        assertThat(foundCar.get().getName()).isEqualTo("Test Car");
    }

    @Test
    public void whenDeleteByIdThenCarShouldBeDeleted() {
        carRepository.save(testCar);
        int id = testCar.getId();

        carRepository.deleteById(id);
        Optional<Car> foundCar = carRepository.findById(id);

        assertThat(foundCar).isNotPresent();
    }

    @Test
    public void whenUpdateThenCarShouldBeUpdated() {
        engineRepository.save(testEngine);
        carRepository.save(testCar);
        testCar.setName("Updated Car");
        testCar.setEngine(testEngine);
        carRepository.update(testCar);
        Optional<Car> updatedCar = carRepository.findById(testCar.getId());
        assertThat(updatedCar).isPresent();
        assertThat(updatedCar.get().getName()).isEqualTo("Updated Car");
    }

    @Test
    public void whenFindAllThenAllCarsAreReturned() {
        carRepository.save(testCar);
        Car anotherCar = new Car();
        anotherCar.setName("Another Car");
        carRepository.save(anotherCar);

        List<Car> cars = carRepository.findAll();
        assertThat(cars).hasSize(2);
        assertThat(cars).extracting(Car::getName).containsExactlyInAnyOrder("Test Car", "Another Car");
    }

    @Test
    public void whenFindByNameThenCarShouldBeFound() {
        carRepository.save(testCar);

        Optional<Car> foundCar = carRepository.findByName("Test Car");
        assertThat(foundCar).isPresent();
        assertThat(foundCar.get().getName()).isEqualTo("Test Car");
    }

    @Test
    public void whenFindByNonExistentNameThenShouldReturnEmpty() {
        Optional<Car> foundCar = carRepository.findByName("Non-existent Car");
        assertThat(foundCar).isNotPresent();
    }
}


