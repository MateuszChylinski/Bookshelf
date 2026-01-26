package com.example.bookshelf.repository;

import com.example.bookshelf.model.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BooksRepository extends JpaRepository<Book, Integer> {}
