package ru.job4j.grabber;

import java.util.List;

/**
 * Store interface
 *
 * @author itfedorovsa (itfedorovsa@gmail.com)
 * @version 1.0
 */
public interface Store {

    /**
     * Save post
     *
     * @param post Post. Type {@link ru.job4j.grabber.Post}
     */
    void save(Post post);

    /**
     * Get all posts
     *
     * @return List of Post. Type {@link java.util.List<ru.job4j.grabber.Post>}
     */
    List<Post> getAll();

    /**
     * Find Post by id
     *
     * @param id Post id
     * @return Post. Type {@link ru.job4j.grabber.Post}
     */
    Post findById(int id);
}
