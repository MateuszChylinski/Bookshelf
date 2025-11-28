package com.example.bookshelf.Model.ErrorModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ErrorDetailed {
    private String message, domain, reason, location, locationType;
}
