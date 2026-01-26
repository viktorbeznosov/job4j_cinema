package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.implementations.Sql2oGenreRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class Sql2oGenreRepositoryTest {
    private static Sql2oGenreRepository sql2oGenreRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();

        try (var inputStream = Sql2oGenreRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }

        var url = properties.getProperty("datasource.url");

        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oGenreRepository = new Sql2oGenreRepository(sql2o);
    }

    @Test
    public void whenFindAll() {
        Collection<Genre> genres = sql2oGenreRepository.findAll();
        assertThat(genres)
                .isNotEmpty()
                .hasSize(5)
                .extracting(Genre::getName)
                .contains("Драма", "Фантастика", "Детектив", "Детское кино", "Комедия");
    }

    @Test
    public void whenGetByName() {
        String genreName = "Фантастика";
        Optional<Genre> genre = sql2oGenreRepository.findByName(genreName);
        assertThat(genre).isNotEmpty();
        assertThat(genre.get()).extracting(Genre::getName).isEqualTo(genreName);
    }
}