package ru.job4j.cinema.repository.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.interfaces.UserRepository;

import java.util.Optional;

@Slf4j
@Repository
public class Sql2oUserRepository implements UserRepository {

    private final Sql2o sql2o;

    public Sql2oUserRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<User> save(User user) {
        try (var connection = sql2o.open()) {
            var sql = """
                      INSERT INTO users (full_name, email, password)
                      VALUES (:fullName, :email, :password)                      
                      """;
            var query = connection.createQuery(sql)
                    .addParameter("fullName", user.getFullName())
                    .addParameter("email", user.getEmail())
                    .addParameter("password", user.getPassword());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            user.setId(generatedId);

            return Optional.of(user);
        } catch (DuplicateKeyException e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM users WHERE email = :email AND password = :password")
                    .addParameter("email", email)
                    .addParameter("password", password);

            var user = query.setColumnMappings(User.COLUMN_MAPPING).executeAndFetchFirst(User.class);

            return Optional.ofNullable(user);
        }
    }
}
