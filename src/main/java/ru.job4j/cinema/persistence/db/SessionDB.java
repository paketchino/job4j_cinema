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
public class SessionDB {

    private static final Logger LOGGER = LogManager.getLogger();

    private final BasicDataSource pool;

    public SessionDB(BasicDataSource pool) {
        this.pool = pool;
    }

    public Optional<Session> add(Session cinema) {
        Optional<Session> rsl = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement pr = cn.prepareStatement(
                     "INSERT INTO sessions (name) VALUES (?)",
             PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            pr.setString(1, cinema.getName());
            pr.execute();
            try (ResultSet id = pr.getGeneratedKeys()) {
                if (id.next()) {
                    cinema.setId(id.getInt(1));
                }
            }
            rsl = Optional.of(cinema);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return rsl;
    }

    public List<Session> listSession() {
        List<Session> sessions = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement preparedStatement =
                     cn.prepareStatement("SELECT * FROM sessions")) {
            try (ResultSet it = preparedStatement.executeQuery()) {
                while (it.next()) {
                    sessions.add(
                            new Session(
                                    it.getInt("id"),
                                    it.getString("name")
                            )
                    );
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return sessions;
    }

    public Session findById(int id) {
        Session rsl = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement preparedStatement =
                     cn.prepareStatement("select * from sessions where id = ? ")
        ) {
            preparedStatement.setInt(1, id);
            try (ResultSet it = preparedStatement.executeQuery()) {
                if (it.next()) {
                    rsl = new Session(
                            it.getInt("id"),
                            it.getString("name")
                    );
                }
            }
        } catch (Exception e) {
            LOGGER.catching(e);
        }
        return rsl;
    }

    public List<Ticket> findTicketsForSessionCellRow(int sessionId, int cell, int row) {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement preparedStatement =
                     cn.prepareStatement("SELECT * FROM ticket "
                             + "WHERE session_id = ? AND pos_row = ? AND cell = ?")
        ) {
            preparedStatement.setInt(1, sessionId);
            preparedStatement.setInt(2, row);
            preparedStatement.setInt(3, cell);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    tickets.add(
                            new Ticket(rs.getInt("id"),
                                    new Session(rs.getInt("id")),
                                    rs.getInt("pos_row"),
                                    rs.getInt("cell"),
                                    new User(rs.getInt("user_id"))
                    ));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return tickets;
    }


}
