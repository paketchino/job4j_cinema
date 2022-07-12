package ru.job4j.cinema.persistence.db;

import org.junit.jupiter.api.Test;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.model.Session;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

public class SessionDBTest {

    @Test
    public void add() {
        Session cinema = new Session(10, "Superman");
        SessionDB sessionDB = new SessionDB(new Main().loadPool());
        assertThat(sessionDB.findById(10), is(cinema));
    }

}