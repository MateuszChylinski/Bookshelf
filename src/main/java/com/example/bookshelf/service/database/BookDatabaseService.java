package com.example.bookshelf.service.database;

import com.example.bookshelf.model.entities.BookEntity;
import com.example.bookshelf.repository.BooksRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BookDatabaseService {

    private final BooksRepository booksRepository;

    public BookEntity saveOrGetBook(BookEntity bookEntity) {
        Optional<BookEntity> existingBook = booksRepository.findByApiId(bookEntity.getApiId());
        String genre = bookEntity.getCategories();

        if (existingBook.isPresent()) {
            return existingBook.get();
        }

        if (genre != null) {
            bookEntity.setCategories(genre.substring(genre.indexOf('[') + 1, genre.indexOf('/') - 1));
        } else {
            bookEntity.setCategories("Unknown categories");
        }

        return booksRepository.save(bookEntity);
    }
}