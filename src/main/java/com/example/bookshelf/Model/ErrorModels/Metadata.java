package com.example.bookshelf.Model.ErrorModels;

public class Metadata {
    private String service;

    public Metadata() {}

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "service='" + service + '\'' +
                '}';
    }
}
