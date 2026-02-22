package com.example.bookshelf.model.entities;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "Book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer savedBookId;
    @Column(name = "api_id", unique = true)
    private String apiId;
    @Column(name = "title")
    private String title;
    @Column(name = "authors")
    private String authors;
    @Column(name = "pages")
    private int pages;
    @Column(name = "description")
    private String description;
    @Column(name = "thumbnail_url", unique = true, length = 1000)
    private String thumbnailUrl;
}


