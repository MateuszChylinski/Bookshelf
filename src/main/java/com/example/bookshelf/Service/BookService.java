package com.example.bookshelf.Service;

import com.example.bookshelf.Components.RandomIndexGenerator;
import com.example.bookshelf.Model.Book;
import com.example.bookshelf.Model.BooksWrapper;
import com.example.bookshelf.Utility.BookUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookService {

    @Value("${books.api_key}")
    private String apikey;
    @Value("${books.api_base_url}")
    private String baseUrl;

    private final RestClient restClient;
    private final RandomIndexGenerator indexGenerator;

    /**
     * Retrieve value entered into top navigation bar, and make an api call with it
     * The api will search for books, that meet user requirements
     * After the call is done, change the link with BookUtility class to change the link to the thumbnail, to avoid blurry book covers
     * If wrapper is not null, then return list with books, otherwise, return empty list
     */

    public List<Book> getBooksForUserQueryQuickSearch(String userQuery) {
        URI uri = UriComponentsBuilder
                .fromUriString(baseUrl)
                .queryParam("q", userQuery)
                .encode()
                .build().toUri();

        BooksWrapper booksWrapper = restClient.get()
                .uri(uri)
                .retrieve()
                .body(BooksWrapper.class);

        if (booksWrapper != null) {
            BookUtility.changeListOfUrls(Collections.singletonList(booksWrapper));
        }
        return booksWrapper != null ? booksWrapper.getBookItems() : List.of();
    }
    // TODO Add enum with categories to the database, to allow randomness

    /**
     * Generate random number that is between 1 and 100. By doing that, api can generate random books each time the main page will be visited.
     * Make an api call, that will return random books. Parameter 'q' will fetch any book that contains letter 'a' in it.
     * After the call is done, change the link with BookUtility class to change the link to the thumbnail, to avoid blurry book covers
     * If wrapper is not null, then return list with books, otherwise, return empty list
     */

    public List<Book> getRandomBooks() {
        int random = indexGenerator.generateStartIndex(1, 30);

        URI uri = UriComponentsBuilder
                .fromUriString(baseUrl)
                .queryParam("q", 'a') // hardcoded param value, just to get random books
                .queryParam("startIndex", random)
                .queryParam("key", apikey)
                .encode()
                .build().toUri();

        BooksWrapper wrapper = restClient.get()
                .uri(uri)
                .retrieve()
                .body(BooksWrapper.class);


        if (wrapper != null) {
            BookUtility.changeListOfUrls(Collections.singletonList(wrapper));
        }

        return wrapper != null ? wrapper.getBookItems() : List.of();
    }


    /**
     * Whenever user will click on any book, this method will be triggered.
     * Prepare an api call with specific book ID.
     * After the call is done, change the link with BookUtility class to change the link to the thumbnail, to avoid blurry book covers
     * If wrapper is not null, then return list with books, otherwise, return empty list
     */

    public Book getBookDetails(String id) {

        URI uri = UriComponentsBuilder
                .fromUriString(baseUrl)
                .path("/{id}")
                .encode()
                .buildAndExpand(id).toUri();

        Book book = restClient.get()
                .uri(uri)
                .retrieve()
                .body(Book.class);

        if (book != null) {
            BookUtility.changeObjectUrl(book);
        }
        return book != null ? book : new Book();
    }
}
