package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.implementations.Sql2oFilmRepository;
import ru.job4j.cinema.repository.implementations.Sql2oGenreRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class Sql2oFilmRepositoryTest {
    private static Sql2oFilmRepository sql2oFilmRepository;

    private static Sql2oGenreRepository sql2oGenreRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();

        try (var inputStream = Sql2oFilmRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }

        var url = properties.getProperty("datasource.url");

        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
        sql2oGenreRepository = new Sql2oGenreRepository(sql2o);
    }

    @Test
    public void whenThenGetOne() {
        Optional<Film> film = sql2oFilmRepository.findById(1);
        Optional<Genre> genre = sql2oGenreRepository.findByName("Драма");
        assertThat(film.isPresent()).isTrue();
        assertThat(film.get().getGenre().getName()).isEqualTo(genre.get().getName());
        assertThat(film.get().getYear()).isEqualTo(1957);
    }

    @Test
    public void whenGetAllFilms() {
        Optional<Film> film1 = sql2oFilmRepository.findById(1);
        Optional<Film> film2 = sql2oFilmRepository.findById(2);
        Collection<Film> films = sql2oFilmRepository.findAll();
        assertThat(films).isNotNull()
            .isNotEmpty()
            .extracting(Film::getName)
            .contains(film1.get().getName(), film2.get().getName())
            .hasSize(25);
    }
}