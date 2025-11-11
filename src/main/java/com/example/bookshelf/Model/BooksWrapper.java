package com.example.bookshelf.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BooksWrapper {

    private String kind;
    private Long totalItems;

    @JsonProperty("items")
    private List<Book> bookItems;
    private Book book;


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

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return "BooksWrapper{" +
                "kind='" + kind + '\'' +
                ", totalItems=" + totalItems +
                ", bookItems=" + bookItems +
                ", book=" + book +
                '}';
    }
}
