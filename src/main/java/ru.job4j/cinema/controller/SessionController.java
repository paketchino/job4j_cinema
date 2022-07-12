package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class SessionController {

    private final SessionService sessionService;
    private final TicketService ticketService;


    public SessionController(SessionService sessionService, TicketService ticketService) {
        this.sessionService = sessionService;
        this.ticketService = ticketService;
    }

    @GetMapping("/sessions")
    public String sessions(Model model, HttpSession session) {
        model.addAttribute("sessions", sessionService.findAll());
        model.addAttribute("user", findUser(session));
        return "sessions";
    }

    @GetMapping("/addSession")
    public String createSession(Model model, HttpSession session) {
        model.addAttribute("user", findUser(session));
        model.addAttribute("cinema", new Session(0, "Enter Name"));
        return "addSession";
    }

    @PostMapping("/createSession")
    public String createSession(@ModelAttribute Session cinema, Model model, HttpSession session) {
        model.addAttribute("user", findUser(session));
        Optional addSession = sessionService.add(cinema);
        return "redirect:/sessions";
    }

    @GetMapping("/postPosRowAndCell/{sessionId}")
    public String postPosRowAndCell(Model model, HttpSession session, @PathVariable("sessionId") int id) {
        model.addAttribute(sessionService.findById(id));
        model.addAttribute("user", findUser(session));
        return "postPos_rowAndCell";
    }

    @GetMapping("/tickets")
    public String tickets(Model model, HttpSession session) {
        model.addAttribute("sessions", ticketService.ticketList());
        model.addAttribute("user", findUser(session));
        return "tickets";
    }

    @GetMapping("/findUserById/{userId}")
    public String findUserById(Model model, HttpSession session, @PathVariable("userId") int uI) {
        model.addAttribute("user", findUser(session));
        model.addAttribute("ticket", ticketService.findUserByTicket(uI));
        return "tickets";

    }
    @PostMapping("/postPosRowAndCell")
    public String postPosRowAndCell(@ModelAttribute Ticket ticket, HttpSession session,
                                     @RequestParam(name = "sessionId") int id,
                                     @RequestParam(name = "pos_Row") int row) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        sessionService.rows();
        sessionService.cells();
        sessionService.findFreeSeat(id, row);
        ticketService.add(ticket);
        return "redirect:/sessions";
    }

    private User findUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        return user;
    }


}
