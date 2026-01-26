package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.service.interfaces.FilmService;
import ru.job4j.cinema.service.interfaces.GenreService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class FilmControllerTest {
    private FilmService filmService;

    private GenreService genreService;

    private FilmController filmController;

    @BeforeEach
    public void initServices() {
        filmService = mock(FilmService.class);
        genreService = mock(GenreService.class);

        filmController = new FilmController(filmService, genreService);
    }

    @Test
    public void whenGetById() {
        Film film = new Film.Builder()
                .id(1)
                .name("Test film")
                .description("Lorem ipsum dolor sit amend")
                .year(2026)
                .genreId(1)
                .minimalAge(18)
                .durationInMinutes(180)
                .build();
        when(filmService.findById(any(Integer.class))).thenReturn(Optional.of(film));

        var model = new ConcurrentModel();
        var view = filmController.getById(model, film.getId());
        var actualFilm = model.getAttribute("film");

        assertThat(view).isEqualTo("films/one");
        assertThat(actualFilm).isEqualTo(film);
    }

    @Test
    public void whenGetFilmsList() {
        Film film1 = new Film.Builder()
                .id(1)
                .name("Test film 1")
                .description("Lorem ipsum dolor sit amend")
                .year(2026)
                .genreId(1)
                .minimalAge(18)
                .durationInMinutes(180)
                .build();
        Film film2 = new Film.Builder()
                .id(2)
                .name("Test film 2")
                .description("Lorem ipsum dolor sit amend")
                .year(2026)
                .genreId(1)
                .minimalAge(18)
                .durationInMinutes(180)
                .build();
        List<Film> expectedFilms = List.of(film1, film2);
        when(filmService.findAll()).thenReturn(expectedFilms);

        var model = new ConcurrentModel();
        var view = filmController.getAll(model);
        var actualFilms = model.getAttribute("films");

        assertThat(view).isEqualTo("films/list");
        assertThat(expectedFilms).isEqualTo(actualFilms);
    }
}