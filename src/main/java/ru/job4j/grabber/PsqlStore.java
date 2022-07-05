package ru.job4j.grabber;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private Connection cnn;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
            String url = cfg.getProperty("jdbc.url");
            String login = cfg.getProperty("jdbc.username");
            String password = cfg.getProperty("jdbc.password");
            cnn = DriverManager.getConnection(url, login, password);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static Properties getProperties(String file) {
        Properties properties = new Properties();
        try (InputStream in = PsqlStore.class.getClassLoader().getResourceAsStream(file)) {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement = cnn.prepareStatement(
                "insert into post (name, text, link, created) values (?, ?, ?, ?);",
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

    @Override
    public List<Post> getAll() {
        List<Post> list = new ArrayList<>();
        try (PreparedStatement statement = cnn.prepareStatement("select * from post;")) {
            try (ResultSet rslSet =  statement.executeQuery()) {
                while (rslSet.next()) {
                    list.add(new Post(
                            rslSet.getInt("id"),
                            rslSet.getString("name"),
                            rslSet.getString("text"),
                            rslSet.getString("link"),
                            rslSet.getTimestamp("created").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Post findById(int id) {
        try (PreparedStatement statement = cnn.prepareStatement(
                "select * from post where id = ?;")) {
            statement.setInt(1, id);
            try (ResultSet rslSet =  statement.executeQuery()) {
                if (rslSet.next()) {
                    return new Post(
                            rslSet.getInt("id"),
                            rslSet.getString("name"),
                            rslSet.getString("text"),
                            rslSet.getString("link"),
                            rslSet.getTimestamp("created").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) {
        Properties properties = getProperties("post.properties");
        PsqlStore store = new PsqlStore(properties);
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
    }
}
