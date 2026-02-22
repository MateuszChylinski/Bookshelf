package com.example.bookshelf.model.rest;

import com.example.bookshelf.model.entities.BookEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    private String id;
    private VolumeInfo volumeInfo;

    public static BookEntity mapToEntity(Book book){
        return BookEntity.builder()
                .apiId(book.getId())
                .title(book.getVolumeInfo().getTitle())
                .authors(String.join(", ", book.getVolumeInfo().getAuthors()))
                .pages(book.getVolumeInfo().getPageCount())
                .description(book.getVolumeInfo().getDescription())
                .thumbnailUrl(book.getVolumeInfo().getImageLinks().getThumbnail())
                .build();
    }
}
