package com.example.bookshelf.Service;

import com.example.bookshelf.Model.Book;
import com.example.bookshelf.Model.BooksWrapper;
import com.example.bookshelf.Model.ErrorModels.Error;
import com.example.bookshelf.Model.ErrorModels.ErrorMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
//@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // To use @BeforeAll/@AfterAll non-static

public class BookServiceTest {

    @Autowired
    private  BookService service;
    private static MockWebServer mockWebServer;

    @BeforeAll
    static void setupServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @DynamicPropertySource
    static void overrideProperty(DynamicPropertyRegistry registry) {
        registry.add("books.api.baseUrl", () ->
                mockWebServer.url("/").toString());

        System.out.println(registry);
    }

    @AfterAll
    void teardown() throws IOException {
        mockWebServer.shutdown();
    }

    // get random books | proper call without errors
    @Test
    void getRandomBooks_shouldReturnBookData() throws IOException, InterruptedException {
        // Given
        String response = Files.readString(Path.of("src/main/resources/JsonResponses/getBooks/getRandomBooksByCategoryProperCall.json"));

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody(response));

        // When
        List<Book> books = service.getRandomBooks();

        // Then - verify request
        RecordedRequest request = mockWebServer.takeRequest();
//        assertThat(request.getPath()).contains("q=a");

        assertThat(response).isNotNull();
        assertThat(books).isNotNull();
//        assertEquals("Grammaire analytique et pratique de la langue polonaise a l'usage des francais par N. Orda", books.getFirst().getVolumeInfo().getTitle());
    }
//
//    // get random books | missing 'q' parameter key, call cannot succeed
//    @Test
//    void getRandomBooks_shouldReturnMissingParameter() throws IOException {
//        String response = Files.readString(Path.of("src/main/resources/JsonResponses/getBooks/getRandomBooksMissingParameter.json"));
//        mockWebServer.enqueue(new MockResponse()
//                .addHeader("Content-Type", "application/json")
//                .setBody(response));
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        ErrorMapper errorResponse = objectMapper.readValue(response, ErrorMapper.class);
//        Error error = errorResponse.getError();
//
//        assertThat(response).isNotNull();
//        assertEquals(400, error.getCode());
//        assertEquals("Required parameter: q", error.getMessage());
//    }
//
//    // get random books | missing parameter value for 'q'. Call cannot succeed
//    @Test
//    void getRandomBooks_shouldReturnErrorMissingQuery() throws IOException {
//        String response = Files.readString(Path.of("src/main/resources/JsonResponses/getBooks/globalMissingQuery.json"));
//
//        mockWebServer.enqueue(new MockResponse()
//                .addHeader("Content-Type", "application/json")
//                .setBody(response));
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        ErrorMapper errorResponse = objectMapper.readValue(response, ErrorMapper.class);
//
//        assertThat(errorResponse).isNotNull();
//        assertEquals(400, errorResponse.getError().getCode());
//        assertEquals("Missing query.", errorResponse.getError().getMessage());
//    }
//
//    // get random books | wrong api key. Call cannot succeed
//    @Test
//    void getRandomBooks_shouldReturnErrorWrongApiKey() throws IOException {
//        String response = Files.readString(Path.of("src/main/resources/JsonResponses/global/GetBooksBadApiKey"));
//
//        mockWebServer.enqueue(new MockResponse()
//                .addHeader("Content-Type", "application/json")
//                .setBody(response));
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        ErrorMapper errorResponse = objectMapper.readValue(response, ErrorMapper.class);
//        Error error = errorResponse.getError();
//
//        assertThat(response).isNotNull();
//        assertEquals(400, error.getCode());
//        assertEquals("API key not valid. Please pass a valid API key.", error.getMessage());
//    }
//
//    // get random books | invalid starting index of -1. Call cannot succeed
//    @Test
//    void getRandomBooks_shouldReturnErrorInvalidValueAtStartingIndex() throws IOException {
//        String response = Files.readString(Path.of("src/main/resources/JsonResponses/getBooks/getRandomBooksStartingIndex=-1.json"));
//
//        mockWebServer.enqueue(new MockResponse()
//                .addHeader("Content-Type", "application/json")
//                .setBody(response));
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        ErrorMapper errorResponse = objectMapper.readValue(response, ErrorMapper.class);
//        Error error = errorResponse.getError();
//
//        assertThat(response).isNotNull();
//        assertEquals(400, error.getCode());
//        assertEquals("Invalid value at 'start_index' (TYPE_UINT32), \"-1\"", error.getMessage());
//    }

    //    // get book detail | set starting index as 999, return totalItems object with value of zero, due to api limit results
//    @Test
//    void getRandomBooks_shouldReturnZeroTotalItems() throws IOException {
//        String response = Files.readString(Path.of("src/main/resources/JsonResponses/getBooks/getRandomBooksStartingIndex999.json"));
//
//        mockWebServer.enqueue(new MockResponse()
//                .addHeader("Content-Type", "application/json")
//                .setBody(response));
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        BooksWrapper booksWrapper = objectMapper.readValue(response, BooksWrapper.class);
//        Long bookCount = booksWrapper.getTotalItems();
//
//        assertThat(response).isNotNull();
//        assertEquals(0, bookCount);
//    }
//
    // get book detail | proper call without errors
    @Test
    void getBookDetails_shouldReturnDetailsAboutBook() throws IOException {
        String response = Files.readString(Path.of("src/main/resources/JsonResponses/getBookDetail/getBookDetailsProperCall.json"));

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(response));

        Book book = service.getBookDetails("id");

        assertThat(response).isNotNull();
        assertThat(book).isNotNull();
        assertEquals("The Google Story (2018 Updated Edition)", book.getVolumeInfo().getTitle());
    }

//    // get book detail | call is missing 'q' parameter
//    @Test
//    void getBookDetails_shouldReturnMissingQParameter() throws IOException {
//        String response = Files.readString(Path.of("src/main/resources/JsonResponses/getBookDetail/getBookDetailsRequiredQ.json"));
//
//
//        mockWebServer.enqueue(new MockResponse()
//                .addHeader("Content-Type", "application/json")
//                .setBody(response));
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        ErrorMapper errorMapper = objectMapper.readValue(response, ErrorMapper.class);
//        Error error = errorMapper.getError();
//
//        assertThat(response).isNotNull();
//        assertEquals("Required parameter: q", error.getMessage());
//        assertEquals(400, error.getCode());
//    }
//
//    // get detailed books | whenever app will try to reach a book with wrong ID, service temporarily unavailable will be thrown
//    @Test
//    void getBookDetails_shouldReturnServiceTemporarilyUnavailable() throws IOException {
//        String response = Files.readString(Path.of("src/main/resources/JsonResponses/getBookDetail/globalServiceTemporarilyUnavailable.json"));
//
//        mockWebServer.enqueue(new MockResponse()
//                .addHeader("Content-Type", "application/json")
//                .setBody(response));
//
//        ObjectMapper mapper = new ObjectMapper();
//        ErrorMapper errorMapper = mapper.readValue(response, ErrorMapper.class);
//        Error error = errorMapper.getError();
//
//        assertThat(response).isNotNull();
//        assertEquals("Service temporarily unavailable.", error.getMessage());
//        assertEquals(503, error.getCode());
//    }
}


//TODO
/*
@Test
public void constructUriWithQueryParameter() {
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
      .scheme("http").host("www.google.com")
      .path("/").query("q={keyword}").buildAndExpand("baeldung");

     assertEquals("http://www.google.com/?q=baeldung", uriComponents.toUriString());
}
Copy

 */