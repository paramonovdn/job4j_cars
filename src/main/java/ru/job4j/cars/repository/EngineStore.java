package ru.job4j.cars.repository;

import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Engine;

import java.util.List;
import java.util.Optional;

public interface EngineStore {

    Optional<Engine> save(Engine engine);

    boolean deleteById(int id);

    boolean update(Engine engine);

    Optional<Engine> findById(int id);

    List<Engine> findAll();

    Optional<Engine> findByName(String name);
}
