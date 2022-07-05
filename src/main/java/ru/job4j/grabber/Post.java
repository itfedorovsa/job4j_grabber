package ru.job4j.grabber;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {

    private int id;
    private String title;
    private String link;
    private String description;
    private LocalDateTime created;

    public Post(int id, String title, String link, String description, LocalDateTime created) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.description = description;
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(title, post.title) && Objects.equals(description, post.description)
                && Objects.equals(created, post.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, created);
    }

    @Override
    public String toString() {
        return String.format("id=%s, title='%s', link='%s', description='%s', created=%s",
                id, title, link, description, created);
    }
}
