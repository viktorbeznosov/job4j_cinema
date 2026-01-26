package ru.job4j.cinema.repository.interfaces;

import ru.job4j.cinema.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> save(User user);

    Optional<User> findByEmailAndPassword(String email, String password);
}
