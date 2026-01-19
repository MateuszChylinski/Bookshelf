package com.example.bookshelf.repository;

import com.example.bookshelf.model.entities.UserBooks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BooksRepository extends JpaRepository<UserBooks, Integer> {}
