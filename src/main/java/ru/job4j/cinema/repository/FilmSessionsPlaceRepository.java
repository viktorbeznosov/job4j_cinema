package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Place;

import java.util.Collection;

public interface FilmSessionsPlaceRepository {
    public Collection<Place> getPlaces(int sessionId);
}
