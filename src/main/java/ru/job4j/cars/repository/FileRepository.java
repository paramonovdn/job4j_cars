package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.File;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class FileRepository implements FileStore {
    private final CrudRepository crudRepository;

    private static final Logger LOG = LoggerFactory.getLogger(FileRepository.class.getName());

    @Override
    public Optional<File> save(File file) {
        try {
            crudRepository.run(session -> session.persist(file));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Optional.empty();
        }
        return Optional.of(file);
    }

    @Override
    public boolean deleteById(int id) {
        try {
            crudRepository.run(
                    "DELETE FROM File f where f.id = :fId",
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return findById(id).isEmpty();
    }


    @Override
    public Optional<File> findById(int id) {
        try {
            return crudRepository.optional(
                    "SELECT f FROM File f WHERE f.id = :fId", File.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<File> findAll() {
        try {
            return crudRepository.query("SELECT f FROM File f", File.class);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }
}
