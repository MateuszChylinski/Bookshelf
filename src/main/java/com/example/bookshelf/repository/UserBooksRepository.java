package com.example.bookshelf.repository;

import com.example.bookshelf.model.entities.BookEntity;
import com.example.bookshelf.model.entities.UserEntity;
import com.example.bookshelf.model.entities.UserBooksEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserBooksRepository extends JpaRepository<UserBooksEntity, Integer> {
    Optional<UserBooksEntity> findByUserEntityAndBookEntity(UserEntity userEntity, BookEntity bookEntity);
    List<UserBooksEntity> deleteByUserEntityAndBookEntity_ApiId(UserEntity userEntity, String bookEntityApiId);
}
