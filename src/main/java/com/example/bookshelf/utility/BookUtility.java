package com.example.bookshelf.utility;

import com.example.bookshelf.model.Book;

public class BookUtility {

    public static void changeObjectUrl(Book book) {
        if (book != null) {
            String id = book.getId();
            book.getVolumeInfo().getImageLinks().setSmallThumbnail(
                    "https://books.google.com/books/publisher/content/" +
                            "images/frontcover/" + id + "?fife=w600-h600&source=gbs_api"
            );
        }
    }
}