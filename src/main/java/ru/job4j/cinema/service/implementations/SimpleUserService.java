package ru.job4j.cinema.service.implementations;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.interfaces.UserRepository;
import ru.job4j.cinema.service.interfaces.UserService;

import java.util.Optional;

@Service
public class SimpleUserService implements UserService {
    private final UserRepository userRepository;

    public SimpleUserService(UserRepository sql2oUserRepository) {
        this.userRepository = sql2oUserRepository;
    }

    @Override
    public Optional<User> save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return this.userRepository.findByEmailAndPassword(email, password);
    }
}
