package ru.job4j.grabber;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Post model
 *
 * @author itfedorovsa (itfedorovsa@gmail.com)
 * @version 1.0
 */
public class Post {

    /**
     * Id
     */
    private int id;

    /**
     * Title
     */
    private String title;

    /**
     * Link
     */
    private String link;

    /**
     * Description
     */
    private String description;

    /**
     * Creation time
     */
    private LocalDateTime created;

    public Post(String title, String link, String description, LocalDateTime created) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.created = created;
    }

    public Post(int id, String title, String link, String description, LocalDateTime created) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.description = description;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
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
        return id == post.id && Objects.equals(link, post.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, link);
    }

    @Override
    public String toString() {
        return String.format("id=%s, title='%s', link='%s', description='%s', created=%s",
                id, title, link, description, created);
    }
}
