package com.example.bookshelf.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BooksWrapper {

    private String kind;
    private Long totalItems;

    @JsonProperty("items")
    private List<Book> bookItems;


    public BooksWrapper() {}

    public BooksWrapper(String kind, Long totalItems, List<Book> bookItems) {
        this.kind = kind;
        this.totalItems = totalItems;
        this.bookItems = bookItems;
    }

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Long totalItems) {
        this.totalItems = totalItems;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public List<Book> getBookItems() {
        return bookItems;
    }

    public void setBookItems(List<Book> bookItems) {
        this.bookItems = bookItems;
    }
}
