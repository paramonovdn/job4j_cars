package ru.job4j;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {

    private UserRepository userRepository;
    private User testUser;
    private SessionFactory sf;
    private StandardServiceRegistry registry;

    @BeforeEach
    public void setUp() {
        registry = new StandardServiceRegistryBuilder()
                .configure() // читает hibernate.cfg.xml
                .build();
        sf = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();

        CrudRepository crudRepository = new CrudRepository(sf);
        userRepository = new UserRepository(crudRepository);

        testUser = new User();
        testUser.setLogin("testuser");
        testUser.setPassword("testpass");
    }

    @AfterEach
    public void tearDown() {
        if (sf != null) {
            sf.close();
        }
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @Test
    public void whenSaveUserThenUserIsSaved() {
        Optional<User> saved = userRepository.save(testUser);
        assertTrue(saved.isPresent());
        assertTrue(saved.get().getId() > 0);
    }

    @Test
    public void whenFindByIdThenReturnUser() {
        // Сохраняем пользователя, чтобы получить id
        User savedUser = userRepository.save(testUser).orElseThrow();
        Optional<User> found = userRepository.findById(savedUser.getId());
        assertTrue(found.isPresent());
        assertEquals(savedUser.getLogin(), found.get().getLogin());
    }

    @Test
    public void whenFindByLoginAndPasswordThenReturnUser() {
        userRepository.save(testUser);
        Optional<User> found = userRepository.findByLoginAndPassword("testuser", "testpass");
        assertTrue(found.isPresent());
        assertEquals(testUser.getLogin(), found.get().getLogin());
    }

    @Test
    public void whenFindAllThenReturnListContainingSavedUser() {
        userRepository.save(testUser);
        List<User> users = userRepository.findAll();
        assertFalse(users.isEmpty());
        assertTrue(users.stream().anyMatch(u -> u.getLogin().equals("testuser")));
    }

    @Test
    public void whenDeleteByIdThenUserIsDeleted() {
        User savedUser = userRepository.save(testUser).orElseThrow();
        boolean deleted = userRepository.deleteById(savedUser.getId());
        assertTrue(deleted);
        Optional<User> found = userRepository.findById(savedUser.getId());
        assertTrue(found.isEmpty());
    }
}
