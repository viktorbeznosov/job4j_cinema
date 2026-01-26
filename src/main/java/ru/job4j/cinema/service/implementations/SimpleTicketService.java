package ru.job4j.cinema.service.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.PlaceDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.interfaces.TicketRepository;
import ru.job4j.cinema.service.interfaces.TicketService;

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
        if (places.size() > 3) {
            throw new Exception("Бронировать можно не более трех билетов");
        }

        ticketRepository.book(places, user);
    }
}
