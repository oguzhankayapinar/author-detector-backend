package com.example.authordetector.repository;

import com.example.authordetector.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findByName(String name);
}
