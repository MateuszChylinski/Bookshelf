package com.example.bookshelf.service.database;

import com.example.bookshelf.exception.BookAlreadyInFavoritesException;
import com.example.bookshelf.model.entities.BookEntity;
import com.example.bookshelf.model.entities.UserEntity;
import com.example.bookshelf.model.entities.UserBooksEntity;
import com.example.bookshelf.repository.UserBooksRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserBooksDatabaseService {

    private UserBooksRepository userBooksRepository;
    private BookDatabaseService bookService;

    public UserBooksEntity addToFavoritesOrThrow(UserEntity userEntity, BookEntity bookEntity) {

        BookEntity savedBookEntity = bookService.saveOrGetBook(bookEntity);

        Optional<UserBooksEntity> findUserAndBook = userBooksRepository.findByUserEntityAndBookEntity(userEntity, savedBookEntity);

        if (findUserAndBook.isPresent()) {
            throw new BookAlreadyInFavoritesException("Book already in favorites");
        } else {
            UserBooksEntity userBooksEntity = UserBooksEntity.builder()
                    .userEntity(userEntity)
                    .bookEntity(savedBookEntity)
                    .isFavorite(true)
                    .added_at(LocalDateTime.now())
                    .build();

            return userBooksRepository.save(userBooksEntity);
        }
    }
}

