package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.persistence.db.UserInDb;
import ru.job4j.cinema.model.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserInDb userInDb;

    public UserService(UserInDb userInDb) {
        this.userInDb = userInDb;
    }

    public Optional<User> add(User user) {
        return userInDb.add(user);
    }

    public Optional<User> findUserByEmail(String email) {
        return find(email);
    }

    private Optional<User> find(String email) {
        Optional<User> findUser = Optional.empty();
        List<User> users = userInDb.users();
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                findUser = Optional.of(user);
            }
        }
        return findUser;
    }

    public User findById(int id) {
        return userInDb.findById(id);
    }

    public List<User> users() {
        return userInDb.users();
    }
}
