package ru.job4j;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.OwnerRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OwnerRepositoryTest {

    private OwnerRepository ownerRepository;
    private Owner testOwner;
    private User testUser;
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // конфигурация берётся из hibernate.cfg.xml
                .build();

        sessionFactory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();

        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        ownerRepository = new OwnerRepository(crudRepository);

        // Создаём и сохраняем User перед созданием Owner
        testUser = new User();
        testUser.setLogin("testUser");
        testUser.setPassword("password123");
        crudRepository.run(session -> session.persist(testUser));

        // Создаем тестовый Owner и привязываем к User
        testOwner = new Owner();
        testOwner.setName("Test Owner");
        testOwner.setUser(testUser);
    }

    @AfterEach
    public void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void whenSaveOwnerThenOwnerHasId() {
        Optional<Owner> savedOwner = ownerRepository.save(testOwner);
        assertTrue(savedOwner.isPresent());
        assertTrue(savedOwner.get().getId() > 0);
        assertEquals("Test Owner", savedOwner.get().getName());
        assertEquals(testUser.getId(), savedOwner.get().getUser().getId());
    }

    @Test
    public void whenFindByIdThenReturnOwner() {
        ownerRepository.save(testOwner);

        Optional<Owner> found = ownerRepository.findById(testOwner.getId());
        assertTrue(found.isPresent());
        assertEquals(testOwner.getName(), found.get().getName());
        assertEquals(testUser.getId(), found.get().getUser().getId());
    }

    @Test
    public void whenUpdateOwnerThenReturnTrueAndUpdated() {
        ownerRepository.save(testOwner);

        testOwner.setName("Updated Owner");
        boolean updated = ownerRepository.update(testOwner);
        assertTrue(updated);

        Optional<Owner> updatedOwner = ownerRepository.findById(testOwner.getId());
        assertTrue(updatedOwner.isPresent());
        assertEquals("Updated Owner", updatedOwner.get().getName());
    }

    @Test
    public void whenDeleteByIdThenOwnerIsDeleted() {
        ownerRepository.save(testOwner);

        boolean deleted = ownerRepository.deleteById(testOwner.getId());
        assertTrue(deleted);

        Optional<Owner> shouldBeEmpty = ownerRepository.findById(testOwner.getId());
        assertFalse(shouldBeEmpty.isPresent());
    }

    @Test
    public void whenFindAllThenReturnListContainingSavedOwner() {
        ownerRepository.save(testOwner);
        List<Owner> allOwners = ownerRepository.findAll();
        assertFalse(allOwners.isEmpty());

        boolean contains = allOwners.stream()
                .anyMatch(owner -> owner.getId() == testOwner.getId());
        assertTrue(contains);
    }

    @Test
    public void whenFindByNameThenReturnOwner() {
        ownerRepository.save(testOwner);

        Optional<Owner> found = ownerRepository.findByName(testOwner.getName());
        assertTrue(found.isPresent());
        assertEquals(testOwner.getId(), found.get().getId());
    }
}
