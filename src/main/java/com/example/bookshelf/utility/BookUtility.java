package com.example.bookshelf.utility;

import com.example.bookshelf.model.rest.Book;

public class BookUtility {

    public static void changeObjectUrl(Book book) {
        if (book == null || book.getVolumeInfo() == null
                || book.getVolumeInfo().getImageLinks() == null) {
            return;
        }
        String id = book.getId();
        book.getVolumeInfo().getImageLinks().setSmallThumbnail(
                "https://books.google.com/books/publisher/content/" +
                        "images/frontcover/" + id + "?fife=w600-h600&source=gbs_api"
        );
    }
}