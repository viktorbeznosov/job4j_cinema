package ru.job4j.cinema.repository;

import ru.job4j.cinema.dto.PlaceDto;
import ru.job4j.cinema.model.Ticket;

import java.util.Collection;
import java.util.Optional;

public interface TicketRepository {
    Optional<Ticket> save(Ticket ticket);

    boolean deleteById(int id);

    Optional<Ticket> findById(int id);

    Collection<Ticket> findBySessionId(int sessionId);

    Collection<Ticket> findAll();

    public Ticket findByPlace(PlaceDto place);
}
