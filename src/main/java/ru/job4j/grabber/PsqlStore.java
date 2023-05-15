package ru.job4j.grabber;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * PostgreSQL Store implementation
 *
 * @author itfedorovsa (itfedorovsa@gmail.com)
 * @version 1.0
 */
public class PsqlStore implements Store, AutoCloseable {

    /**
     * Connection
     */
    private final Connection connection;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
            String url = cfg.getProperty("jdbc.url");
            String login = cfg.getProperty("jdbc.username");
            String password = cfg.getProperty("jdbc.password");
            connection = DriverManager.getConnection(url, login, password);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Get properties
     *
     * @param file Properties file. Type {@link java.lang.String}
     * @return Properties. Type {@link java.util.Properties}
     */
    private static Properties getProperties(String file) {
        Properties properties = new Properties();
        try (InputStream in = PsqlStore.class.getClassLoader().getResourceAsStream(file)) {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * @param rslSet ResultSet. Type {@link java.sql.ResultSet}
     * @return Post. Type {@link ru.job4j.grabber.Post}
     * @throws SQLException SQLException
     */
    private static Post getPost(ResultSet rslSet) throws SQLException {
        return new Post(
                rslSet.getInt("id"),
                rslSet.getString("name"),
                rslSet.getString("link"),
                rslSet.getString("text"),
                rslSet.getTimestamp("created").toLocalDateTime()
        );
    }

    /**
     * Save post
     *
     * @param post Post. Type {@link ru.job4j.grabber.Post}
     */
    @Override
    public void save(Post post) {
        try (PreparedStatement statement = connection.prepareStatement(
                "insert into post (name, link, text, created) values (?, ?, ?, ?) on conflict (link) do nothing;",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getLink());
            statement.setString(3, post.getDescription());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            statement.execute();
            try (ResultSet rslSet = statement.getGeneratedKeys()) {
                if (rslSet.next()) {
                    post.setId(rslSet.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all posts
     *
     * @return List of Post. Type {@link java.util.List<ru.job4j.grabber.Post>}
     */
    @Override
    public List<Post> getAll() {
        List<Post> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("select * from post;")) {
            try (ResultSet rslSet = statement.executeQuery()) {
                while (rslSet.next()) {
                    list.add(getPost(rslSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Find Post by id
     *
     * @param id Post id
     * @return Post. Type {@link ru.job4j.grabber.Post}
     */
    @Override
    public Post findById(int id) {
        try (PreparedStatement statement = connection.prepareStatement(
                "select * from post where id = ?;")) {
            statement.setInt(1, id);
            try (ResultSet rslSet = statement.executeQuery()) {
                if (rslSet.next()) {
                    return getPost(rslSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Close connection
     *
     * @throws Exception Exception
     */
    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * Main method
     *
     * @param args App arguments
     */
    public static void main(String[] args) {
        Properties properties = getProperties("post.properties");
        try (PsqlStore store = new PsqlStore(properties)) {
            LocalDateTime ldt1 = LocalDateTime.now();
            LocalDateTime ldt2 = ldt1.minusMinutes(1);
            store.save(new Post("vacancy1", "link1", "description1", ldt1));
            store.save(new Post("vacancy2", "link2", "description2", ldt2));
            Post findedById = store.findById(2);
            System.out.println(findedById);
            List<Post> list = store.getAll();
            for (Post l : list) {
                System.out.println(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
