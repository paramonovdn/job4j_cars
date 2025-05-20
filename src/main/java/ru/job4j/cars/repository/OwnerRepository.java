package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Owner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class OwnerRepository implements OwnerStore {

    private final CrudRepository crudRepository;

    private static final Logger LOG = LoggerFactory.getLogger(OwnerRepository.class.getName());

    @Override
    public Optional<Owner> save(Owner owner) {
        try {
            crudRepository.run(session -> session.persist(owner));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.of(owner);
    }

    @Override
    public boolean deleteById(int id) {
        try {
            crudRepository.run(
                    "DELETE FROM Owner where id = :fId",
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return findById(id).isEmpty();
    }

    @Override
    public boolean update(Owner owner) {
        try {
            crudRepository.run(session -> session.merge(owner));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        var updatedOwner = findById(owner.getId()).get();
        return  (owner.getName().equals(updatedOwner.getName()));
    }

    @Override
    public Optional<Owner> findById(int id) {
        try {
            return crudRepository.optional(
                    "SELECT DISTINCT o FROM Owner o LEFT JOIN FETCH o.user where o.id = :fId", Owner.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Owner> findAll() {
        try {
            return crudRepository.query("SELECT DISTINCT o FROM Owner o LEFT JOIN FETCH o.user ", Owner.class);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<Owner> findByName(String name) {
        try {
            return crudRepository.optional(
                    "SELECT DISTINCT o FROM Owner o LEFT JOIN FETCH o.user where o.name = :fName", Owner.class,
                    Map.of("fName", name)
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.empty();
    }
}
