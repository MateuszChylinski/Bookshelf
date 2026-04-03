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
    @Column(name = "title", length = 1024)
    private String title;
    @Column(name = "authors")
    private String authors;
    @Column(name = "pagesCount")
    private int pagesCount;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "thumbnail_url", columnDefinition = "TEXT")
    private String thumbnailUrl;
    @Column(name = "genres", columnDefinition = "TEXT")
    private String categories;
}

