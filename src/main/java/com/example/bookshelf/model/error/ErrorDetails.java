package com.example.bookshelf.model.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDetails {
    @JsonProperty("@type")
    private String errorType;
    private String domain, reason, locale, message;
    private Metadata metadata;
}
