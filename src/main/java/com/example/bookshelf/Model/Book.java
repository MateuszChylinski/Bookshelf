package com.example.bookshelf.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    private VolumeInfo volumeInfo;

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }
}
