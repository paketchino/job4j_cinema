package ru.job4j.cinema.persistence.db;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TicketDb {

    private static final Logger LOGGER = LogManager.getLogger();

    private final BasicDataSource pool;

    public TicketDb(BasicDataSource pool) {
        this.pool = pool;
    }

    public Optional<Ticket> add(Ticket ticket) {
        Optional<Ticket> rsl = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement preparedStatement =
                     cn.prepareStatement("insert into ticket " +
                                     "(session_id, pos_row, cell, user_id)" + "values (?, ?, ?, ?)",
                             PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setInt(1, ticket.getSession().getId());
            preparedStatement.setInt(2, ticket.getPos_row());
            preparedStatement.setInt(3, ticket.getCell());
            preparedStatement.setInt(4, ticket.getUser().getId());
            preparedStatement.execute();
            try (ResultSet id = preparedStatement.getGeneratedKeys()) {
                if (id.next()) {
                    ticket.setId(id.getInt(1));
                }
            }
            rsl = Optional.of(ticket);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return rsl;
    }

    public List<Ticket> findById(int movieId, int pos_row) {
       List<Ticket> rsl = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement preparedStatement =
                     cn.prepareStatement("select * from ticket where movie_id = ? where pos_row = ?")
        ) {
            preparedStatement.setInt(1, movieId);
            preparedStatement.setInt(2, pos_row);
            try (ResultSet it = preparedStatement.executeQuery()) {
                if (it.next()) {
                    rsl.add(new Ticket(
                                    it.getInt("id"),
                                    new Session(it.getInt("session_id"), null),
                                    it.getInt("pos_row"),
                                    it.getInt("cell"),
                                    new User(it.getInt("users_id"))
                    ));
                }
            }
        } catch (Exception e) {
            LOGGER.catching(e);
        }
        return rsl;
    }


    public List<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement preparedStatement =
                     cn.prepareStatement("SELECT * FROM ticket")) {
            try (ResultSet it = preparedStatement.executeQuery()) {
                while (it.next()) {
                    tickets.add(
                            new Ticket(
                                    it.getInt("id"),
                                    new Session(it.getInt("session_id")),
                                            it.getInt("pos_row"),
                                            it.getInt("cell"),
                                            new User(it.getInt("user_id"))
                            )
                    );
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return tickets;
    }

    public List<Ticket> findTicketByUser(int id) {
        List<Ticket> rsl = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement preparedStatement =
                     cn.prepareStatement("select * from ticket where user_id = ? ")
        ) {
            preparedStatement.setInt(1, id);
            try (ResultSet it = preparedStatement.executeQuery()) {
                if (it.next()) {
                    rsl.add(new Ticket(
                            it.getInt("id"),
                            new Session(it.getInt("session_id"), null),
                            it.getInt("pos_row"),
                            it.getInt("cell"),
                            new User(it.getInt("users_id"))
                    ));
                }
            }
        } catch (Exception e) {
            LOGGER.catching(e);
        }
        return rsl;
    }


}
