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

    // TODO Add enum with categories to the database, to allow randomness

    /**
     * Generate random number that is between 1 and 200. By doing that, api can generate random books each time the main page will be visited.
     * Make an api call, that will return random books. Parameter 'q' will fetch any book that contains letter 'a in it.
     * After the call is done, change the link with BookUtility class to change the link to the thumbnail, to avoid blurry book covers
     * If wrapper is not null, then return list with books, otherwise, return empty list
     */

    public List<Book> getAllFictionBooks() {
        int random = (int) (Math.random() * 200) + 1;
        BooksWrapper wrapper = restClient.get()
                .uri("/books/v1/volumes?q=a&startIndex="
                        + random + "&maxResult=40&key=" + apikey)
                .retrieve()
                .body(BooksWrapper.class);

        if (wrapper != null) {
            BookUtility.changeUrl(Collections.singletonList(wrapper));
        }

        return wrapper != null ? wrapper.getBookItems() : List.of();
    }
}
