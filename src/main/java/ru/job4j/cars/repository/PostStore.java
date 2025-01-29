package ru.job4j.cars.repository;


import ru.job4j.cars.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostStore {
    List<Post> findLastDayPosts();
    List<Post> findPostWithPhoto();
    List<Post> findPostDistinctModel(String carName);
}
