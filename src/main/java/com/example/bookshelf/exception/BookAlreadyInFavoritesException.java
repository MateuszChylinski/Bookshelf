package com.example.bookshelf.exception;

public class BookAlreadyInFavoritesException extends RuntimeException {
    public BookAlreadyInFavoritesException(String message) {
        super(message);
    }
}
