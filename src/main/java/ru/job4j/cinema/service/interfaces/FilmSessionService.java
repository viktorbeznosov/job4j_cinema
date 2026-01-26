package ru.job4j.cinema.service.interfaces;

import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Place;

import java.util.Collection;
import java.util.Optional;

public interface FilmSessionService {
    Optional<FilmSession> findById(int id);

    Collection<FilmSession> findAll();

    Collection<Place> getPlaces(int sessionId);
}
