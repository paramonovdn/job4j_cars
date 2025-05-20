package ru.job4j.cars.repository;

import ru.job4j.cars.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStore {

    Optional<User> save(User user);

    Optional<User> findById(int id);

    boolean deleteById(int id);

    Optional<User> findByLoginAndPassword(String login, String password);

    Collection<User> findAll();
}
