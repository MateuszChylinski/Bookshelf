package com.example.bookshelf.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BookNotRemovedFromFavoritesException extends RuntimeException {
    public BookNotRemovedFromFavoritesException(String message) {
        super(message);
    }
}
