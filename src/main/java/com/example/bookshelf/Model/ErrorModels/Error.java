package com.example.bookshelf.Model.ErrorModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Error {
    private int code;
    private String message, status;
    @JsonProperty("errors")
    private List<ErrorDetailed> errorsList;
    @JsonProperty("details")
    private List<ErrorDetails> errorDetailsList;
}