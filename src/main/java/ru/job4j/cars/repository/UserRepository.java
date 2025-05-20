package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.User;

import java.util.*;

@Repository
@AllArgsConstructor
public class UserRepository implements UserStore {
    private final CrudRepository crudRepository;

    private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class);

    @Override
    public Optional<User> save(User user) {
        try {
            crudRepository.run(session -> session.persist(user));
            return Optional.of(user);
        } catch (Exception e) {
            LOG.error("Error saving user: " + e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findById(int id) {
        try {
            return crudRepository.optional(
                    "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.participates  where u.id = :fId", User.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(int id) {
        try {
            crudRepository.run(
                    "DELETE FROM User where id = :fId",
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return findById(id).isEmpty();
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        try {
            return crudRepository.optional(
                    "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.participates WHERE u.login = :fLogin AND u.password = :fPassword", User.class,
                    Map.of(
                            "fLogin", login,
                            "fPassword", password
                    )
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        try {
            return crudRepository.query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.participates", User.class);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }
}