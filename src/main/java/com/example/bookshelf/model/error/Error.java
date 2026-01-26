package com.example.bookshelf.model.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Error {
    private int code;
    private String message, status;
    @JsonProperty("errors")
    private List<ErrorDetailed> errorsList;
    @JsonProperty("details")
    private List<ErrorDetails> errorDetailsList;
}