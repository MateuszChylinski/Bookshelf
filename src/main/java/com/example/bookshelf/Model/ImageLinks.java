package com.example.bookshelf.Model;

public class ImageLinks {
    private String smallThumbnail, thumbnail;

    public ImageLinks() {}

    public String getSmallThumbnail() {
        return smallThumbnail;
    }

    public void setSmallThumbnail(String smallThumbnail) {
        this.smallThumbnail = smallThumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "ImageLinks{" +
                "smallThumbnail='" + smallThumbnail + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
