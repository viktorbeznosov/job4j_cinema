package ru.job4j.cinema.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.cinema.service.interfaces.FileService;

@Slf4j
@RestController
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        var contentOptional = fileService.getFileById(id);
        if (contentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contentOptional.get().getContent());
    }

    @GetMapping("get_by_name/{name}")
    public ResponseEntity<?> getByName(@PathVariable String name) {
        var contentOptional = fileService.getFileByName(name);
        if (contentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contentOptional.get().getContent());
    }
}
