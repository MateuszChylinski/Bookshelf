package com.example.bookshelf.exception;

public class BookNotOnTheShelfException extends RuntimeException {
    public BookNotOnTheShelfException(String message){
        super(message);
    }
}

