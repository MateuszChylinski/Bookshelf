package com.example.bookshelf.model.records;

import com.example.bookshelf.model.entities.Status;

public record UserBookDetailsRecord(Status bookStatus, Integer bookRating, Boolean isFavorite) {
}
