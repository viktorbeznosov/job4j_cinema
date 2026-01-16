package ru.job4j.cinema.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Place;
import ru.job4j.cinema.repository.FilmSessionRepository;
import ru.job4j.cinema.repository.FilmSessionsPlaceRepository;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class SimpleFilmSessionService implements FilmSessionService {
    private final FilmSessionRepository filmSessionRepository;
    private final FilmSessionsPlaceRepository filmSessionsPlaceRepository;

    public SimpleFilmSessionService(FilmSessionRepository sql2pFilmSessionRepository, FilmSessionsPlaceRepository filmSessionsPlaceRepository) {
        this.filmSessionRepository = sql2pFilmSessionRepository;
        this.filmSessionsPlaceRepository = filmSessionsPlaceRepository;
    }

    @Override
    public Optional<FilmSession> findById(int id) {
        return filmSessionRepository.findById(id);
    }

    @Override
    public Collection<FilmSession> findAll() {
        return filmSessionRepository.findAll();
    }

    @Override
    public Collection<Place> getPlaces(int sessionId) {
        return filmSessionsPlaceRepository.getPlaces(sessionId);
    }
}
