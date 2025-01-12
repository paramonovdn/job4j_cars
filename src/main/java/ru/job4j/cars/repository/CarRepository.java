package ru.job4j.cars.repository;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CarRepository implements CarStore {
    private final CrudRepository crudRepository;

    private static final Logger LOG = LoggerFactory.getLogger(CarRepository.class.getName());

    @Override
    public Optional<Car> save(Car car) {
        try {
            crudRepository.run(session -> session.persist(car));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.of(car);
    }

    @Override
    public boolean deleteById(int id) {
        try {
            crudRepository.run(
                    "DELETE FROM Car where id = :fId",
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return findById(id).isEmpty();
    }

    @Override
    public boolean update(Car car) {
        try {
            crudRepository.run(session -> session.merge(car));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        var updatedCar = findById(car.getId()).get();
        return  (car.getName().equals(updatedCar.getName())
                && car.getEngine().equals(updatedCar.getEngine()));
    }

    @Override
    public Optional<Car> findById(int id) {
        try {
            return crudRepository.optional(
                    "SELECT DISTINCT c FROM Car c LEFT JOIN FETCH c.engine LEFT JOIN FETCH c.owners where c.id = :fId", Car.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Car> findAll() {
        try {
            return crudRepository.query("SELECT DISTINCT c FROM Car c LEFT JOIN FETCH c.engine "
                    + "LEFT JOIN FETCH c.owners", Car.class);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<Car> findByName(String name) {
        try {
            return crudRepository.optional(
                    "SELECT DISTINCT c FROM Car c LEFT JOIN FETCH c.engine LEFT JOIN FETCH c.owners where c.name = :fName", Car.class,
                    Map.of("fName", name)
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.empty();
    }
}
