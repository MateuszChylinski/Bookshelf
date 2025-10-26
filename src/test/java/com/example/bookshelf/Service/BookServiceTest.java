package com.example.bookshelf.Service;

import com.example.bookshelf.Model.Book;
import com.example.bookshelf.Model.BooksWrapper;
import com.example.bookshelf.Model.ErrorModels.Error;
import com.example.bookshelf.Model.ErrorModels.ErrorMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BookServiceTest {

    private BookService service;
    private MockWebServer mockWebServer;

    @BeforeEach
    void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        RestClient restClient = RestClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        service = new BookService(restClient);
    }

    @AfterEach
    void teardown() throws IOException {
        mockWebServer.shutdown();
    }

    // proper call without errors
    @Test
    void shouldReturnBookData() throws IOException {
        String response = Files.readString(Path.of("src/main/resources/JsonResponses/getBooks/getBooksByCategoryProperCall"));

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(response));

        List<Book> books = service.getRandomBooks();

        assertThat(response).isNotNull();
        assertThat(!books.isEmpty());
        assertEquals("Grammaire analytique et pratique de la langue polonaise a l'usage des francais par N. Orda", books.getFirst().getVolumeInfo().getTitle());
    }

    // missing 'q' parameter key, call cannot succeed
    @Test
    void shouldReturnMissingParameter() throws IOException {
        String response = Files.readString(Path.of("src/main/resources/JsonResponses/getBooks/getBooksMissingParameter"));
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(response));

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorMapper errorResponse = objectMapper.readValue(response, ErrorMapper.class);
        Error error = errorResponse.getError();

        assertThat(response).isNotNull();
        assertEquals(400, error.getCode());
        assertEquals("Required parameter: q", error.getMessage());
    }

    // missing parameter value for 'q'. Call cannot succeed
    @Test
    void shouldReturnErrorMissingQuery() throws IOException {
        String response = Files.readString(Path.of("src/main/resources/JsonResponses/getBooks/getBooksMissingQuery"));

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(response));

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorMapper errorResponse = objectMapper.readValue(response, ErrorMapper.class);

        assertThat(errorResponse).isNotNull();
        assertEquals(400, errorResponse.getError().getCode());
        assertEquals("Missing query.", errorResponse.getError().getMessage());
    }

    // wrong api key. Call cannot succeed
    @Test
    void shouldReturnErrorWrongApiKey() throws IOException {
        String response = Files.readString(Path.of("src/main/resources/JsonResponses/getBooks/GetBooksBadApiKey"));

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(response));

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorMapper errorResponse = objectMapper.readValue(response, ErrorMapper.class);
        Error error = errorResponse.getError();

        assertThat(response).isNotNull();
        assertEquals(400, error.getCode());
        assertEquals("API key not valid. Please pass a valid API key.", error.getMessage());
    }

    // invalid starting index of -1. Call cannot succeed
    @Test
    void shouldReturnErrorInvalidValueAtStartingIndex() throws IOException {
        String response = Files.readString(Path.of("src/main/resources/JsonResponses/getBooks/getBooksStartingIndex=-1"));

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(response));

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorMapper errorResponse = objectMapper.readValue(response, ErrorMapper.class);
        Error error = errorResponse.getError();

        assertThat(response).isNotNull();
        assertEquals(400, error.getCode());
        assertEquals("Invalid value at 'start_index' (TYPE_UINT32), \"-1\"", error.getMessage());
    }

    // set starting index as 999, return totalItems object with value of zero, due to api limit results
    @Test
    void shouldReturnZeroTotalItems() throws IOException {
        String response = Files.readString(Path.of("src/main/resources/JsonResponses/getBooks/getBooksStartingIndex999"));

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(response));

        ObjectMapper objectMapper = new ObjectMapper();
        BooksWrapper booksWrapper = objectMapper.readValue(response, BooksWrapper.class);
        Long bookCount = booksWrapper.getTotalItems();

        assertThat(response).isNotNull();
        assertEquals(0, bookCount);
    }
}
