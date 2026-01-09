package com.example.bookshelf.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Books")
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int saved_book_id;
    @Column(name = "api_id")
    private String api_id;
    @Column(name = "title")
    private String title;
    @Column(name = "authors")
    private String authors;
    @Column(name = "pages")
    private int pages;
    @Column(name = "description")
    private String description;
    @Column(name = "thumbnail_url")
    private String thumbnail_url;

    public Books(int savedBookId, String apiId, String title, String authors, int pages, String description, String thumbnailUrl) {
        this.saved_book_id = savedBookId;
        this.api_id = apiId;
        this.title = title;
        this.authors = authors;
        this.pages = pages;
        this.description = description;
        this.thumbnail_url = thumbnailUrl;
    }
}


