package ru.job4j;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.EngineRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class EngineRepositoryTest {
    private EngineRepository engineRepository;
    private Engine testEngine;

    @BeforeEach
    public void setUp() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory();
        CrudRepository crudRepository = new CrudRepository(sf);
        engineRepository = new EngineRepository(crudRepository);

        testEngine = new Engine();
        testEngine.setName("Test Engine");
    }

    @Test
    public void whenSaveEngineThenReturnsEngine() {
        Optional<Engine> saved = engineRepository.save(testEngine);
        assertTrue(saved.isPresent());
        assertEquals(testEngine.getName(), saved.get().getName());
    }

    @Test
    public void whenFindByIdThenReturnEngine() {
        engineRepository.save(testEngine);
        Optional<Engine> found = engineRepository.findById(testEngine.getId());
        assertTrue(found.isPresent());
        assertEquals(testEngine.getName(), found.get().getName());
    }

    @Test
    public void whenUpdateEngineThenReturnTrue() {
        engineRepository.save(testEngine);
        testEngine.setName("Updated Engine");
        boolean updated = engineRepository.update(testEngine);
        assertTrue(updated);

        Optional<Engine> updatedEngine = engineRepository.findById(testEngine.getId());
        assertTrue(updatedEngine.isPresent());
        assertEquals("Updated Engine", updatedEngine.get().getName());
    }

    @Test
    public void whenDeleteByIdThenReturnTrue() {
        engineRepository.save(testEngine);
        boolean deleted = engineRepository.deleteById(testEngine.getId());
        assertTrue(deleted);
        Optional<Engine> shouldBeEmpty = engineRepository.findById(testEngine.getId());
        assertTrue(shouldBeEmpty.isEmpty());
    }

    @Test
    public void whenFindAllThenReturnListOfEngines() {
        engineRepository.save(testEngine);
        List<Engine> engines = engineRepository.findAll();
        assertFalse(engines.isEmpty());
        assertTrue(engines.stream().anyMatch(e -> e.getName().equals(testEngine.getName())));
    }

    @Test
    public void whenFindByNameThenReturnEngine() {
        engineRepository.save(testEngine);
        Optional<Engine> found = engineRepository.findByName("Test Engine");
        assertTrue(found.isPresent());
        assertEquals(testEngine.getName(), found.get().getName());
    }
}
