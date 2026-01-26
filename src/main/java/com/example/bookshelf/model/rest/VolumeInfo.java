package com.example.bookshelf.model.rest;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class VolumeInfo {
    private String title, publisher, publishedDate, description;
    private int pageCount;
    private List<String> authors;
    private ImageLinks imageLinks;
    private List<String> categories;
}

