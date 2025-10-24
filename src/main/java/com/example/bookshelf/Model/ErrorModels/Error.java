package com.example.bookshelf.Model.ErrorModels;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Error {
    private int code;
    private String message, status;
    @JsonProperty("errors")
    private List<ErrorDetailed> errorsList;
    @JsonProperty("details")
    private List<ErrorDetails> errorDetailsList;

    public Error() {}

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ErrorDetailed> getErrorsList() {
        return errorsList;
    }

    public void setErrorsList(List<ErrorDetailed> errorsList) {
        this.errorsList = errorsList;
    }

    public List<ErrorDetails> getErrorDetailsList() {
        return errorDetailsList;
    }

    public void setErrorDetailsList(List<ErrorDetails> errorDetailsList) {
        this.errorDetailsList = errorDetailsList;
    }
}