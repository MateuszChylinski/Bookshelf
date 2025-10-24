package com.example.bookshelf.Service;

import com.example.bookshelf.Model.Book;
import com.example.bookshelf.Model.BooksWrapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class BookService {

    private final RestClient restClient;

    public BookService(RestClient client) {
        this.restClient = client;
    }


//    public List<Book> getAllFictionBooks(){
//        BooksWrapper wrapper = restClient.get()
//                .uri("/books/v1/volumes?q=fiction")
//                .retrieve()
//                .body(BooksWrapper.class);
//
//        return wrapper != null ? wrapper.getBookItems() : List.of();
//    }
}
