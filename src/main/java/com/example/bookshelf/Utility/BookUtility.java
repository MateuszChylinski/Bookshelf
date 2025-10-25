package com.example.bookshelf.Utility;

import com.example.bookshelf.Model.Book;
import com.example.bookshelf.Model.BooksWrapper;

import java.util.List;

public class BookUtility {

    public static void changeUrl(List<BooksWrapper> booksWrapperList) {
        for (BooksWrapper booksWrapper : booksWrapperList) {
            List<Book> books = booksWrapper.getBookItems();
            for (Book book : books) {
                String id = book.getId();
                book.getVolumeInfo().getImageLinks().setSmallThumbnail(
                        "https://books.google.com/books/publisher/content/" +
                                "images/frontcover/" + id + "?fife=w600-h600&source=gbs_api"
                );
            }
        }
    }
}