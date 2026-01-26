package ru.job4j.cinema.repository.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.interfaces.GenreRepository;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class Sql2oGenreRepository implements GenreRepository {

    private final Sql2o sql2o;

    public Sql2oGenreRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Genre> findById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM genres WHERE id = :id")
                    .addParameter("id", id);
            var genre = query.executeAndFetchFirst(Genre.class);
            return Optional.ofNullable(genre);
        } catch (Sql2oException e) {
            log.error("Ошибка при поиске жанра по ID {}: {}", id, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Genre> findByName(String name) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM genres WHERE name = :name")
                    .addParameter("name", name);

            var genre = query.executeAndFetchFirst(Genre.class);
            return Optional.ofNullable(genre);
        } catch (Sql2oException e) {
            log.error("Ошибка при поиске жанра по названию '{}': {}", name, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Collection<Genre> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM genres ORDER BY name");

            var genres = query.executeAndFetch(Genre.class);
            return genres;
        } catch (Sql2oException e) {
            log.error("Ошибка при получении всех жанров: {}", e.getMessage(), e);
            return java.util.Collections.emptyList();
        }
    }
}