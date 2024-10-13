package com.example.authordetector.controller;

import com.example.authordetector.dto.AuthorResponse;
import com.example.authordetector.model.Author;
import com.example.authordetector.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        return ResponseEntity.ok(authorService.saveAuthor(author));
    }

    @GetMapping("/with-posts")
    public ResponseEntity<List<AuthorResponse>> getAllAuthorsWithPosts() {
        List<Author> authors = authorService.getAllAuthors();
        List<AuthorResponse> authorResponses = authors.stream()
                .map(author -> new AuthorResponse(
                        author.getName(),
                        author.getPosts().stream()
                                .map(post -> post.getContent())
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(authorResponses);
    }

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, String>> analyzeText(@RequestBody String text) {
        Author matchingAuthor = authorService.findMatchingAuthor(text);
        String responseMessage;
        if (matchingAuthor != null) {
            responseMessage = "Bu metin " + matchingAuthor.getName() + " tarafından yazılmıştır.";
        } else {
            responseMessage = "Yazar bulunamadı.";
        }
        return ResponseEntity.ok(Map.of("message", responseMessage));
    }


}
