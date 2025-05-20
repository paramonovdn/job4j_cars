package ru.job4j;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.File;

import ru.job4j.cars.repository.CrudRepository;

import ru.job4j.cars.repository.FileRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class FileRepositoryTest {
    private FileRepository fileRepository;
    private File testFile;

    @BeforeEach
    public void setUp() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory();
        CrudRepository crudRepository = new CrudRepository(sf);
        fileRepository = new FileRepository(crudRepository);

        testFile = new File();
        testFile.setName("testName");
        testFile.setPath("/tmp/testpath");
    }

    @Test
    public void whenSaveFileThenReturnFileWithId() {
        Optional<File> saved = fileRepository.save(testFile);
        assertTrue(saved.isPresent());
        assertTrue(saved.get().getId() > 0);
        assertEquals(testFile.getName(), saved.get().getName());
        assertEquals(testFile.getPath(), saved.get().getPath());
    }

    @Test
    public void whenFindByIdExistingIdThenReturnFile() {
        // Сначала сохраняем файл
        fileRepository.save(testFile);
        int id = testFile.getId();
        Optional<File> found = fileRepository.findById(id);
        assertTrue(found.isPresent());
        assertEquals(testFile, found.get());
    }

    @Test
    public void whenFindByIdNonExistingIdThenReturnEmpty() {
        Optional<File> found = fileRepository.findById(-1);
        assertTrue(found.isEmpty());
    }

    @Test
    public void whenFindAllThenReturnListIncludingSavedFile() {
        fileRepository.save(testFile);
        List<File> files = fileRepository.findAll();
        assertFalse(files.isEmpty());
        assertTrue(files.contains(testFile));
    }

    @Test
    public void whenDeleteByIdExistingIdThenReturnTrueAndFileNotFound() {
        fileRepository.save(testFile);
        int id = testFile.getId();

        boolean deleted = fileRepository.deleteById(id);
        assertTrue(deleted);
        assertTrue(fileRepository.findById(id).isEmpty());
    }

    @Test
    public void whenDeleteByIdNonExistingIdThenReturnTrue() {
        boolean deleted = fileRepository.deleteById(-1);
        // Удаление несуществующего id в вашем коде все равно возвращает true, так как проверяется findById
        assertTrue(deleted);
    }
}
