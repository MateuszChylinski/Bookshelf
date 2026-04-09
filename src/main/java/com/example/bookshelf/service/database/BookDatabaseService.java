package com.example.bookshelf.service.database;

import com.example.bookshelf.model.entities.BookEntity;
import com.example.bookshelf.model.records.UserStatistics;
import com.example.bookshelf.repository.BooksRepository;
import com.example.bookshelf.repository.UserBooksRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BookDatabaseService {

    private final BooksRepository booksRepository;

    public BookEntity saveOrGetBook(BookEntity bookEntity) {
        Optional<BookEntity> existingBook = booksRepository.findByApiId(bookEntity.getApiId());

        if (existingBook.isPresent()) {
            return existingBook.get();
        } else {
            String genre = bookEntity.getCategories();
            bookEntity.setCategories(genre.substring(genre.indexOf('[') + 1, genre.indexOf('/') - 1));

            return booksRepository.save(bookEntity);
        }
    }
}