package ru.job4j.cinema.controller;

import lombok.extern.slf4j.Slf4j;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.TicketService;

@Slf4j
@ThreadSafe
@Controller
@RequestMapping("/film_sessions")
public class FilmSessionController {
    private final FilmSessionService filmSessionService;

    private final TicketService ticketService;

    public FilmSessionController(
            FilmSessionService filmSessionService,
            TicketService ticketService
    ) {
        this.filmSessionService = filmSessionService;
        this.ticketService = ticketService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("filmSessions", filmSessionService.findAll());
        return "film_sessions/list";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var filmSessionOptional = filmSessionService.findById(id);
        var purchasedTickets = ticketService.findBySessionId(id);
        var places = filmSessionService.getPlaces(id);
        if (filmSessionOptional.isEmpty()) {
            model.addAttribute("message", "Фильм не найден");
            return "errors/404";
        }
        model.addAttribute("filmSession", filmSessionOptional.get());
        model.addAttribute("purchasedTickets", purchasedTickets);
        model.addAttribute("places", places);

        return "film_sessions/one";
    }
}
