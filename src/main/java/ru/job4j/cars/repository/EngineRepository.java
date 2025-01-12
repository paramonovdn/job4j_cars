package ru.job4j.cars.repository;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class EngineRepository implements EngineStore {

    private final CrudRepository crudRepository;

    private static final Logger LOG = LoggerFactory.getLogger(EngineRepository.class.getName());

    @Override
    public Optional<Engine> save(Engine engine) {
        try {
            crudRepository.run(session -> session.persist(engine));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.of(engine);
    }

    @Override
    public boolean deleteById(int id) {
        try {
            crudRepository.run(
                    "DELETE FROM Engine where id = :fId",
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return findById(id).isEmpty();
    }

    @Override
    public boolean update(Engine engine) {
        try {
            crudRepository.run(session -> session.merge(engine));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        var updatedEngine = findById(engine.getId()).get();
        return  (engine.getName().equals(updatedEngine.getName()));
    }

    @Override
    public Optional<Engine> findById(int id) {
        try {
            return crudRepository.optional(
                    "FROM Engine e where e.id = :fId", Engine.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Engine> findAll() {
        try {
            return crudRepository.query("FROM Engine", Engine.class);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<Engine> findByName(String name) {
        try {
            return crudRepository.optional(
                    "SELECT DISTINCT e FROM Engine e where e.name = :fName", Engine.class,
                    Map.of("fName", name)
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

}
