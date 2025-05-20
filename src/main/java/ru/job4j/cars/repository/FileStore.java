package ru.job4j.cars.repository;


import ru.job4j.cars.model.File;

import java.util.List;
import java.util.Optional;

public interface FileStore {

    Optional<File> save(File file);

    boolean deleteById(int id);

    Optional<File> findById(int id);

    List<File> findAll();

}
