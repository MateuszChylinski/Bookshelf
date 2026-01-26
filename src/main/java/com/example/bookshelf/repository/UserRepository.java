package com.example.bookshelf.repository;

import com.example.bookshelf.model.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
}
