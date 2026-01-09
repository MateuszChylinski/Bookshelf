package com.example.bookshelf.repository;

import com.example.bookshelf.model.entities.Books;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedBooksRepository extends JpaRepository<Books, Integer> {}
