package com.example.bookshelf.Service;

import com.example.bookshelf.Model.Book;
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

    @Test
    void shouldReturnBookData() throws IOException {
        String response = Files.readString(Path.of("src/main/resources/JsonResponses/getBooksByCategory"));

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(response));

        List<Book> books = service.getAllFictionBooks();

        assertThat(response).isNotNull();
        assertThat(!books.isEmpty());
        assertEquals("Effective Java", books.getFirst().getVolumeInfo().getTitle());
    }

    @Test
    void shouldReturnMissingParameter() throws IOException {
        String response = Files.readString(Path.of("src/main/resources/JsonResponses/getBooksMissingParameter"));

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

    @Test
    void shouldReturnErrorMissingQuery() throws IOException {
        String response = Files.readString(Path.of("src/main/resources/JsonResponses/getBooksMissingQuery"));

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(response));

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorMapper errorResponse = objectMapper.readValue(response, ErrorMapper.class);

        assertThat(errorResponse).isNotNull();
        assertEquals(400, errorResponse.getError().getCode());
        assertEquals("Missing query.", errorResponse.getError().getMessage());
    }

    @Test
    void shouldReturnErrorWrongApiKey() throws IOException {
        String response = Files.readString(Path.of("src/main/resources/JsonResponses/GetBooksBadApiKey"));

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
}
