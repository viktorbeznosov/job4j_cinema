package ru.job4j.cinema.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.cinema.dto.PlaceDto;
import ru.job4j.cinema.model.*;
import ru.job4j.cinema.repository.Sql2oTicketRepository;
import ru.job4j.cinema.repository.TicketRepository;
import ru.job4j.cinema.service.SimpleTicketService;
import ru.job4j.cinema.service.TicketService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
class TicketControllerTest {
    private TicketRepository ticketRepository;

    private TicketService ticketService;

    private TicketController ticketController;

    private Hall hall;

    private Film film;

    private FilmSession session;

    @BeforeEach
    public void initServices() {
        ticketRepository = mock(TicketRepository.class);

        ticketService = new SimpleTicketService(ticketRepository);

        ticketController = new TicketController(ticketService);

        hall = new Hall(1, "Test hall", 50, 50, "test");
        film = new Film.Builder()
                .id(1)
                .name("Test film")
                .description("Lorem ipsum dolor sit amend")
                .year(2026)
                .genreId(1)
                .minimalAge(18)
                .durationInMinutes(180)
                .build();
        session = new FilmSession();
        session.setId(1);
        session.setFilmId(1);
        session.setHallsId(1);
        session.setStartTime(LocalDateTime.of(2026, 01, 20, 20, 00));
        session.setEndTime(LocalDateTime.of(2026, 01, 20, 22, 00));
        session.setPrice(2000);
    }

    @Test
    public void whenBookTicketsUnauthorizedUser() throws JsonProcessingException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        request.setAttribute("seatsData", "[{\"sessionId\":1,\"rowNumber\":2,\"placeNumber\":1},{\"sessionId\":1,\"rowNumber\":2,\"placeNumber\":2}]");
        request.setAttribute("sessionId", session.getId());
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        HttpSession session = mock(HttpSession.class);
        var view = ticketController.book(request, redirectAttributes, session);

        assertThat(view).isEqualTo("redirect:/login");
    }

    @Test
    public void whenBookReservedSeats() throws Exception {
        Ticket ticket = new Ticket(1, 1, 1, 1, 1);
        ticketService.save(ticket);
        User user = new User(1, "Test", "test@mail.ru", "12345");
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("seatsData")).thenReturn("[{\"sessionId\":1,\"rowNumber\":1,\"placeNumber\":1}]");
        when(request.getParameter("sessionId")).thenReturn(String.valueOf(session.getId()));

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute("user")).thenReturn(user);
        when(request.getSession()).thenReturn(httpSession);
        when(ticketRepository.findByPlace(any(PlaceDto.class))).thenReturn(ticket);

        var view = ticketController.book(request, redirectAttributes, httpSession);

        assertThat(view).isEqualTo(String.format("redirect:/film_sessions/%s", session.getId()));
        verify(redirectAttributes).addFlashAttribute("result", "error");
        verify(redirectAttributes).addFlashAttribute("message", "Билет уже был забронирован");
    }

    @Test
    public void whenBookFreeTicketsAuthorizedUser() throws JsonProcessingException {
        Ticket ticket = new Ticket(1, 1, 1, 1, 1);
        User user = new User(1, "Test", "test@mail.ru", "12345");
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("seatsData")).thenReturn("[{\"sessionId\":1,\"rowNumber\":1,\"placeNumber\":1}]");
        when(request.getParameter("sessionId")).thenReturn(String.valueOf(session.getId()));

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute("user")).thenReturn(user);
        when(request.getSession()).thenReturn(httpSession);
        when(ticketRepository.findByPlace(any(PlaceDto.class))).thenReturn(null);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(Optional.of(ticket));

        var view = ticketController.book(request, redirectAttributes, httpSession);

        assertThat(view).isEqualTo(String.format("redirect:/film_sessions/%s", session.getId()));
        verify(redirectAttributes).addFlashAttribute("result", "success");
        verify(redirectAttributes).addFlashAttribute("message", "Билеты успешно забронированы");
    }
}