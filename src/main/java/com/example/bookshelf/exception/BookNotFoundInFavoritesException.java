package com.example.bookshelf.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BookNotFoundInFavoritesException extends RuntimeException{
    public BookNotFoundInFavoritesException(String message) {
        super(message);
    }
}
