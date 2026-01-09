package com.example.bookshelf.repository;

import com.example.bookshelf.model.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Integer> {}
