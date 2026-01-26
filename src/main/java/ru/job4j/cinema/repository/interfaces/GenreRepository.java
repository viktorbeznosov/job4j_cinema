package ru.job4j.cinema.repository.interfaces;

import ru.job4j.cinema.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreRepository {
    Optional<Genre> findById(int id);

    Optional<Genre> findByName(String name);

    Collection<Genre> findAll();
}
