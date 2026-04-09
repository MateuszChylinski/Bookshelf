package com.example.bookshelf.repository;

import com.example.bookshelf.model.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BooksRepository extends JpaRepository<BookEntity, Integer> {

    boolean findByTitle(String title);

    Optional<BookEntity> findByApiId(String apiId);
}
