package ru.job4j.cinema.service.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.repository.interfaces.FilmRepository;
import ru.job4j.cinema.service.interfaces.FileService;
import ru.job4j.cinema.service.interfaces.FilmService;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class SimpleFilmService implements FilmService {
    private final FilmRepository filmRepository;

    private final FileService fileService;

    public SimpleFilmService(FilmRepository sql2oFilmRepository, FileService fileService) {
        this.filmRepository = sql2oFilmRepository;
        this.fileService = fileService;
    }

    @Override
    public Optional<Film> findById(int id) {
        return filmRepository.findById(id);
    }

    @Override
    public Collection<Film> findAll() {
        return filmRepository.findAll();
    }
}
