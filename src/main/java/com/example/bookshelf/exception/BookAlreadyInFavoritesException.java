package com.example.bookshelf.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BookAlreadyInFavoritesException extends RuntimeException {
    public BookAlreadyInFavoritesException(String message) {
        super(message);
    }
}
