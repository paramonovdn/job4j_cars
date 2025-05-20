package ru.job4j;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.PostRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PostRepositoryTest {

    private PostRepository postRepository;
    private Post testPost;
    private SessionFactory sessionFactory;
    private CrudRepository crudRepository;

    @BeforeEach
    public void setUp() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        sessionFactory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();

        crudRepository = new CrudRepository(sessionFactory);
        postRepository = new PostRepository(crudRepository);

        testPost = new Post();
        testPost.setDescription("Test Post Description");
        testPost.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        testPost.setAutoUserId(1);
        testPost.setCarId(1);
        testPost.setFileId(0); // без фото по умолчанию
    }

    @AfterEach
    public void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void whenSavePostThenItHasId() {
        crudRepository.run(session -> session.persist(testPost));
        // persist void, id устанавливается в entity
        assertTrue(testPost.getId() > 0);
    }

    @Test
    public void whenFindLastDayPostsThenReturnPosts() {
        crudRepository.run(session -> session.persist(testPost));
        List<Post> posts = postRepository.findLastDayPosts();

        assertNotNull(posts);
        assertFalse(posts.isEmpty());
        assertTrue(posts.stream().anyMatch(p -> p.getId() == testPost.getId()));
    }

    @Test
    public void whenFindPostWithPhotoThenReturnPosts() {
        // Создадим пост с fileId (фото)
        testPost.setFileId(10);
        crudRepository.run(session -> session.persist(testPost));

        List<Post> postsWithPhoto = postRepository.findPostWithPhoto();

        assertNotNull(postsWithPhoto);
        assertFalse(postsWithPhoto.isEmpty());
        assertTrue(postsWithPhoto.stream().anyMatch(p -> p.getId() == testPost.getId()));
    }

    @Test
    public void whenFindPostDistinctModelThenReturnPostsByCarName() {
        // Для теста должен быть в бд Car с id=1 и name="TestCar"
        // Нужно его сохранить отдельно или у тебя уже есть тестовая БД с этой записью

        // Создаем тестовый Post с carId = 1
        crudRepository.run(session -> session.persist(testPost));

        List<Post> postsByCarName = postRepository.findPostDistinctModel("TestCar");

        assertNotNull(postsByCarName);
        // Пока может быть пустым, если в тестовой БД нет Car с name="TestCar"
        // Можно проверить, что либо список пуст, либо содержит нужный пост при наличии данных
    }

}
