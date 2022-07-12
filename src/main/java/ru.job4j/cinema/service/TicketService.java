package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.persistence.db.TicketDb;
import ru.job4j.cinema.model.Ticket;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketDb ticketDb;

    public TicketService(TicketDb ticketDb) {
        this.ticketDb = ticketDb;
    }

    public Optional<Ticket> add(Ticket ticket) {
        return ticketDb.add(ticket);
    }

    public List<Ticket> findById(int movieId, int posRow) {
        return ticketDb.findById(movieId, posRow);
    }

    public List<Ticket> ticketList() {
        return ticketDb.findAll();
    }

    public List<Ticket> findUserByTicket(int id) {
        return ticketDb.findTicketByUser(id);
    }
}
