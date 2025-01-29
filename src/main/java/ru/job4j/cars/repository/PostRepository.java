package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PostRepository implements PostStore {

    private final CrudRepository crudRepository;

    private static final Logger LOG = LoggerFactory.getLogger(PostRepository.class.getName());
    @Override
    public List<Post> findLastDayPosts() {
        try {
            var today = Timestamp.valueOf(LocalDateTime.now()).toString().substring(0, 10);
            var posts = crudRepository.query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.messengers "
                    + "WHERE p.created LIKE: today", Post.class,
                    Map.of("today", today)
                    );
            return posts;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Post> findPostWithPhoto() {
        try {
            var posts = crudRepository.query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.messengers "
                            + "WHERE p.fileId IS NOT NULL", Post.class);
            return posts;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Post> findPostDistinctModel(String carName) {
        try {
            var posts = crudRepository.query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.messengers "
                            + "JOIN Car c ON p.carId = c.id WHERE c.name = :carName", Post.class,
                    Map.of("carName", carName)
            );
            return posts;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

}
