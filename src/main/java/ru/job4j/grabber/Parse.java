package ru.job4j.grabber;

import java.util.List;

/**
 * Parse interface
 *
 * @author itfedorovsa (itfedorovsa@gmail.com)
 * @version 1.0
 */
public interface Parse {

    /**
     * Create list of posts
     *
     * @param link Site link. Type {@link java.lang.String}
     * @return List of Post. Type {@link java.util.List<ru.job4j.grabber.Post>}
     */
    List<Post> list(String link);

}
