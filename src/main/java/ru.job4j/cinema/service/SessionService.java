package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.persistence.db.SessionDB;
import ru.job4j.cinema.persistence.db.TicketDb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ThreadSafe
@Service
public class SessionService {

    private static final Logger LOGGER = LogManager.getLogger();
    private final SessionDB sessionDB;
    private static final int ROWS = 5;
    private static final int CELLS = 5;
    private final TicketDb ticketDb;


    public SessionService(SessionDB sessionDB, TicketDb ticketDb) {
        this.sessionDB = sessionDB;
        this.ticketDb = ticketDb;
    }

    public Optional<Session> add(Session cinema) {
        return sessionDB.add(cinema);
    }

    public List<Session> findAll() {
        return sessionDB.listSession();
    }

    public Session findById(int id) {
        return sessionDB.findById(id);
    }


    public List<Integer> rows() {
        List<Integer> row = new ArrayList<>();
        for (int i = 0; i < ROWS; i++) {
            row.add(i + 1);
        }
        return row;
    }

    public List<Integer> cells() {
        List<Integer> cells = new ArrayList<>();
        for (int i = 0; i < CELLS; i++) {
            cells.add(i + 1);
        }
        return cells;
    }


    private List<Ticket> getPosAndMovieId(int pos, int movie) {
        return ticketDb.findById(movie, pos);
    }


    public List<Integer> findFreeSeat(int movieId, int posRow) {
        List<Integer> cells = cells();
        var getPosAndMovieId = getPosAndMovieId(movieId, posRow);
        for (Ticket t : getPosAndMovieId) {
            if (cells.contains(t.getCell())) {
                cells().remove(Integer.valueOf(t.getCell()));
            }
        }
        return cells;
    }

}
