package com.example.bookshelf.repository;

import com.example.bookshelf.model.entities.BookEntity;
import com.example.bookshelf.model.entities.Status;
import com.example.bookshelf.model.entities.UserEntity;
import com.example.bookshelf.model.entities.UserBooksEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserBooksRepository extends JpaRepository<UserBooksEntity, Integer> {
    Optional<UserBooksEntity> findByUserEntityAndBookEntity(UserEntity userEntity, BookEntity bookEntity);

    List<UserBooksEntity> deleteByUserEntityAndBookEntity_ApiId(UserEntity userEntity, String bookEntityApiId);

    @Query("SELECT SUM (ub.bookEntity.pagesCount) FROM UserBooksEntity ub WHERE ub.userEntity = :user AND ub.status = :status")
    Integer countAllBookPagesRead(@Param("user") UserEntity userEntity, @Param("status") Status status);

    @Query("SELECT COUNT(ub) FROM UserBooksEntity ub WHERE ub.status = :status AND ub.userEntity = :user")
    Integer getAllBooksRead(@Param("user") UserEntity entity, @Param("status") Status status);

    @Query("SELECT ub.bookEntity.categories FROM UserBooksEntity ub WHERE ub.status = :status AND ub.userEntity = :user GROUP BY ub.bookEntity.categories ORDER BY COUNT(ub.bookEntity.categories) DESC")
    String getFavoriteGenre(@Param("user") UserEntity entity, @Param("status") Status status, PageRequest pageRequest);

    @Query("SELECT COUNT(ub.status) FROM UserBooksEntity ub WHERE ub.userEntity = :entity")
    Integer getCountOfFavoritesBooks(@Param("entity") UserEntity userEntity);

    @Query("SELECT ub.bookEntity FROM UserBooksEntity ub WHERE ub.status = :status AND ub.userEntity = :user")
    List<BookEntity> getFinishedBooks(@Param("status") Status status, @Param("user") UserEntity userEntity);

}
