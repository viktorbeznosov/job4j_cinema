package ru.job4j.cinema.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.cinema.dto.PlaceDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.interfaces.TicketService;

import java.util.List;

@Slf4j
@ThreadSafe
@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/book")
    public String book(
            HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            HttpSession session,
            Model model
    ) throws JsonProcessingException {
        var user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        String seatsJson = request.getParameter("seatsData");
        String sessionId = request.getParameter("sessionId");

        if (seatsJson == null || sessionId == null) {
            throw new IllegalArgumentException("Missing required parameters");
        }

        ObjectMapper mapper = new ObjectMapper();
        List<PlaceDto> places = mapper.readValue(seatsJson,
                mapper.getTypeFactory().constructCollectionType(List.class, PlaceDto.class));

        try {
            this.ticketService.book(places, user);
            redirectAttributes.addFlashAttribute("result", "success");
            redirectAttributes.addFlashAttribute("message", "Билеты успешно забронированы");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/409";
        }

        return String.format("redirect:/film_sessions/%s", sessionId);
    }
}
