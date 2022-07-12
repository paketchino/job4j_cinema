package ru.job4j.cinema.model;

import java.util.Objects;

public class Ticket {

    private int id;
    private Session session;
    private int posRow;
    private int cell;
    private User user;

    public Ticket() {
    }

    public Ticket(int id, Session session, int posRow, int cell, User user) {
        this.id = id;
        this.session = session;
        this.posRow = posRow;
        this.cell = cell;
        this.user = user;
    }

    public Ticket(int ticketId, int id, int row, int cell, User byId) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session cinema) {
        this.session = cinema;
    }

    public int getPosRow() {
        return posRow;
    }

    public void setPosRow(int posRow) {
        this.posRow = posRow;
    }

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ticket ticket = (Ticket) o;
        return id == ticket.id
                && posRow == ticket.posRow
                && cell == ticket.cell
                && Objects.equals(session, ticket.session)
                && Objects.equals(user, ticket.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, session, posRow, cell, user);
    }
}
