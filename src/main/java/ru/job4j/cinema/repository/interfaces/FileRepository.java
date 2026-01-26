package ru.job4j.cinema.repository.interfaces;

import ru.job4j.cinema.model.File;
import java.util.Optional;

public interface FileRepository {

    File save(File file);

    Optional<File> findById(int id);

    Optional<File> findByName(String name);

    void deleteById(int id);
}
