package com.example.authordetector.service;


import com.example.authordetector.model.Author;
import com.example.authordetector.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.*;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author saveAuthor(Author author) {
        Author existingAuthor = authorRepository.findByName(author.getName());
        if (existingAuthor != null) {
            if (author.getPosts() != null) {
                for (var post : author.getPosts()) {
                    post.setAuthor(existingAuthor);
                    existingAuthor.getPosts().add(post);
                }
            }
            return authorRepository.save(existingAuthor);
        } else {
            if (author.getPosts() != null) {
                for (var post : author.getPosts()) {
                    post.setAuthor(author);
                }
            }
            return authorRepository.save(author);
        }
    }
    private static final Set<String> CONJUNCTIONS = Set.of(
            "ve", "ile", "ancak", "ama", "fakat", "lakin", "oysa", "veya", "ya", "ne", "ki", "gibi"
    );

    private List<String> filterWords(String text) {
        return Arrays.stream(text.toLowerCase().split("\\W+"))
                .filter(word -> !CONJUNCTIONS.contains(word))
                .collect(Collectors.toList());
    }
    private Map<String, Long> getWordFrequency(List<String> words) {
        return words.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public Map<String, Long> analyzeFrequentWords(Author author) {
        List<String> allWords = author.getPosts().stream()
                .flatMap(post -> filterWords(post.getContent()).stream())
                .collect(Collectors.toList());
        return getWordFrequency(allWords).entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(30)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Author findMatchingAuthor(String inputText) {
        List<String> inputWords = filterWords(inputText);

        Author bestMatch = null;
        long maxMatchCount = 0;

        for (Author author : authorRepository.findAll()) {
            Map<String, Long> authorFrequentWords = analyzeFrequentWords(author);
            long matchCount = inputWords.stream()
                    .filter(authorFrequentWords::containsKey)
                    .count();

            if (matchCount > maxMatchCount) {
                maxMatchCount = matchCount;
                bestMatch = author;
            }
        }
        return bestMatch;
    }




    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
}
