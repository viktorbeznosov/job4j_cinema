package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.PlaceDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TicketService {
    Optional<Ticket> save(Ticket ticket);

    boolean deleteById(int id);

    Optional<Ticket> findById(int id);

    Collection<Ticket> findBySessionId(int sessionId);

    Collection<Ticket> findAll();

    void book(List<PlaceDto> places, User user) throws Exception;
}
