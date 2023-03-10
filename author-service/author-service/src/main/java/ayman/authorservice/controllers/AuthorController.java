package ayman.authorservice.controllers;

import ayman.authorservice.exception.AuthorNotFoundException;
import ayman.authorservice.models.Author;
import ayman.authorservice.models.AuthorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class AuthorController {

    private List<Author> authors;

    public AuthorController() {
        this.authors = new ArrayList<>();

        this.authors.add(new Author(1, "Wendell Adriel", "wendelladriel.ti@gmail.com", AuthorType.EDITOR));
        this.authors.add(new Author(2, "John McQueide", "mcqueide@gmail.com", AuthorType.WRITER));
    }

    @GetMapping(path = "/{id}")
    public Author findById(@PathVariable("id") Integer id) {
        log.info(String.format("Authors.findById(%d)", id));
        return this.authors.stream()
                .filter(aut -> aut.getId().intValue() == id.intValue())
                .findFirst()
                .orElseThrow(() -> new AuthorNotFoundException("id : " + id));
    }

    @GetMapping(path = "/")
    public List<Author> getAll() {
        log.info("Authors.getAll()");
        return this.authors;
    }

    public List<Author> getAllCached() {
        log.info("Authors.getAllCached()");
        log.warn("Return cached result here");

        return new ArrayList<>();
    }
}
