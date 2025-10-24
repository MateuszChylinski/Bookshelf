package com.example.bookshelf.Model.ErrorModels;

public class ErrorMapper {
    private Error error;

    public ErrorMapper() {
    }

    public ErrorMapper(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
