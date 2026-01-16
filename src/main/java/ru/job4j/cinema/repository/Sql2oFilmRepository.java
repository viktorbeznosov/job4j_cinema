package ru.job4j.cinema.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.sql2o.ResultSetHandler;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.job4j.cinema.dto.FileDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class Sql2oFilmRepository implements FilmRepository {
    private final Sql2o sql2o;

    public Sql2oFilmRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Film> findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = """
                    SELECT films.*, genres.name as genre_name, genres.id as genre_id FROM films 
                    LEFT JOIN genres on genres.id = films.genre_id
                    WHERE films.id = :id                    
                    """;
            var query = connection.createQuery(sql);
            var result = query
                    .addParameter("id", id)
                    .executeAndFetchFirst((ResultSetHandler<Film>) resultSet -> {
                        Genre genre = new Genre();
                        genre.setId(resultSet.getInt("genre_id"));
                        genre.setName(resultSet.getString("genre_name"));

                        Film film = new Film.Builder()
                            .id(resultSet.getInt("id"))
                            .name(resultSet.getString("name"))
                            .description(resultSet.getString("description"))
                            .year(resultSet.getInt("year"))
                            .genreId(resultSet.getInt("genre_id"))
                            .minimalAge(resultSet.getInt("minimal_age"))
                            .durationInMinutes(resultSet.getInt("duration_in_minutes"))
                            .fileId(resultSet.getInt("file_id"))
                            .genre(genre)
                            .build();

                        return film;
                    });
            return Optional.ofNullable(result);
        }
    }

    @Override
    public Collection<Film> findAll() {
        try (var connection = sql2o.open()) {
            var sql = """
                    SELECT films.*, genres.name as genre_name  FROM films
                    LEFT JOIN genres on genres.id = films.genre_id
                    ORDER BY films.id ASC
                    """;

            return connection.createQuery(sql)
                .executeAndFetch((ResultSetHandler<Film>) resultSet -> {
                    Genre genre = new Genre();
                    genre.setId(resultSet.getInt("genre_id"));
                    genre.setName(resultSet.getString("genre_name"));

                    Film film = new Film.Builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .description(resultSet.getString("description"))
                        .year(resultSet.getInt("year"))
                        .genreId(resultSet.getInt("genre_id"))
                        .minimalAge(resultSet.getInt("minimal_age"))
                        .durationInMinutes(resultSet.getInt("duration_in_minutes"))
                        .fileId(resultSet.getInt("file_id"))
                        .genre(genre)
                        .build();

                    return film;
                });
        }
    }
}
