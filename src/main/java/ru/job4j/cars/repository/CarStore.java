package ru.job4j.cars.repository;

import ru.job4j.cars.model.Car;

import java.util.List;
import java.util.Optional;

public interface CarStore {
    Optional<Car> save(Car car);

    boolean deleteById(int id);

    boolean update(Car car);

    Optional<Car> findById(int id);

    List<Car> findAll();

    Optional<Car> findByName(String name);

}
