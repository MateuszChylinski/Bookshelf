package com.example.bookshelf.service;

import com.example.bookshelf.components.RandomIndexGenerator;
import com.example.bookshelf.model.Book;
import com.example.bookshelf.model.BooksMapper;
import com.example.bookshelf.utility.BookUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class BookService {

    private final String apikey;
    private final String baseUrl;
    private final RestClient restClient;
    private final RandomIndexGenerator indexGenerator;

    public BookService(
            @Value("${books.api_key}") String apikey,
            @Value("${books.api_base_url}") String baseUrl,
            RestClient restClient,
            RandomIndexGenerator indexGenerator) {
        this.apikey = apikey;
        this.baseUrl = baseUrl;
        this.restClient = restClient;
        this.indexGenerator = indexGenerator;
    }

    private List<Book> getBooks(URI uri) {
        BooksMapper wrapper = restClient.get()
                .uri(uri)
                .retrieve()
                .body(BooksMapper.class);


        if (wrapper == null || wrapper.getBookItems() == null || wrapper.getBookItems().isEmpty()) {
            return List.of();
        }

        for (int i = 0; i < wrapper.getBookItems().size(); i++) {
            if (wrapper.getBookItems().get(i) != null) {
                BookUtility.changeObjectUrl(wrapper.getBookItems().get(i));

            }
        }
        return wrapper.getBookItems();
    }

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
                .queryParam("key", apikey)
                .encode()
                .build().toUri();

        return getBooks(uri);
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

        return getBooks(uri);
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
                .queryParam("key", apikey)
                .encode()
                .buildAndExpand(id).toUri();

        Book book = restClient.get()
                .uri(uri)
                .retrieve()
                .body(Book.class);

        if (book == null || book.getVolumeInfo() == null) {
            return new Book();
        }
        BookUtility.changeObjectUrl(book);

        return book;
    }
}
