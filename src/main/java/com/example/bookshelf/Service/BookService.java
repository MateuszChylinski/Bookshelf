package com.example.bookshelf.Service;

import com.example.bookshelf.Model.Book;
import com.example.bookshelf.Model.BooksWrapper;
import com.example.bookshelf.Utility.BookUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@Service
public class BookService {

    @Value("${api_key}")
    private String apikey;

    private final RestClient restClient;

    public BookService(RestClient client) {
        this.restClient = client;
    }


    public List<Book> getAllFictionBooks() {

        BooksWrapper wrapper = restClient.get()
                .uri("/books/v1/volumes?q=fiction&key=" + apikey)
                .retrieve()
                .body(BooksWrapper.class);

        if (wrapper != null) {
            BookUtility.changeUrl(Collections.singletonList(wrapper));
        }

        return wrapper != null ? wrapper.getBookItems() : List.of();
    }
}
