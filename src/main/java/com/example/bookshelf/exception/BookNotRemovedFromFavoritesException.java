package com.example.bookshelf.exception;

public class BookNotRemovedFromFavoritesException extends RuntimeException {
    public BookNotRemovedFromFavoritesException(String message) {
        super(message);
    }
}
