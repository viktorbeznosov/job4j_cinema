package ru.job4j.cinema.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.PlaceDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SimpleTicketService implements TicketService {
    private final TicketRepository ticketRepository;

    public SimpleTicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Optional<Ticket> save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public boolean deleteById(int id) {
        return ticketRepository.deleteById(id);
    }

    @Override
    public Optional<Ticket> findById(int id) {
        return ticketRepository.findById(id);
    }

    @Override
    public Collection<Ticket> findBySessionId(int sessionId) {
        return ticketRepository.findBySessionId(sessionId);
    }

    @Override
    public Collection<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    @Override
    public void book(List<PlaceDto> places, User user) throws Exception {
        for (PlaceDto place: places) {
            Optional<Ticket> existingTicket = Optional.ofNullable(ticketRepository.findByPlace(place));
            if (existingTicket.isPresent()) {
                throw new Exception("Билет уже был забронирован");
            }
        }
        for (PlaceDto place: places) {
            Ticket ticket = new Ticket();
            ticket.setSessionId(place.getSessionId());
            ticket.setPlaceNumber(place.getPlaceNumber());
            ticket.setRowNumber(place.getRowNumber());
            ticket.setUserId(user.getId());

            var result = ticketRepository.save(ticket);
            if (result.isEmpty()) {
                throw new Exception("Ошибка бронирования билета");
            }
        }
    }
}
