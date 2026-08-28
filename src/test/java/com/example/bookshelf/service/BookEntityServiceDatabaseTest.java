package com.example.bookshelf.service;

import com.example.bookshelf.components.RandomIndexGenerator;
import com.example.bookshelf.model.rest.Book;
import com.example.bookshelf.service.rest.BookRestService;
import com.example.bookshelf.util.TestUtils;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookEntityServiceDatabaseTest {

    @Mock
    private RandomIndexGenerator indexGenerator;

    private MockWebServer mockWebServer;
    private BookRestService service;

    @BeforeEach
    void setupServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String baseUrl = mockWebServer.url("/").toString();
        RestClient restClient = RestClient.builder().build();

        service = new BookRestService(("testApiKey"), baseUrl, restClient, indexGenerator);
    }

    @AfterEach
    void teardown() throws IOException {
        mockWebServer.shutdown();
    }

    // RANDOM BOOKS

    // get random books | parameterized error scenarios
    @ParameterizedTest
    @MethodSource("com.example.bookshelf.util.TestUtils#errorScenarios")
    void getRandomBooks_parameterizedTests(
            String jsonFile,
            HttpStatus httpStatus,
            String errorMessage
    ) throws Exception {
        String response = TestUtils.loadJsonFromResource(jsonFile);

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(httpStatus.value())
                .addHeader("Content-Type", "application/json")
                .setBody(response)
        );

        assertThatThrownBy(() -> service.getRandomBooks())
                .isInstanceOf(HttpStatusCodeException.class)
                .satisfies(exception -> {
                    HttpStatusCodeException statusCodeException = (HttpStatusCodeException) exception;
                    assertThat(statusCodeException.getStatusCode()).isEqualTo(httpStatus);
                    assertThat(statusCodeException.getResponseBodyAsString()).contains(errorMessage);
                });
    }

    // get random books | proper call without errors
    @Test
    void getRandomBooks_shouldReturnBookData() throws IOException, InterruptedException {

        // Given
        String response = TestUtils.loadJsonFromResource(
                "jsonResponses/getRandomBooks/getRandomBooksByCategoryProperCall.json"
        );

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody(response));

        when(indexGenerator.generateStartIndex(anyInt(), anyInt())).thenReturn(5);

        // When
        List<Book> books = service.getRandomBooks();

        // Then - verify request
        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getPath()).contains("q=a");
        assertThat(request.getPath()).contains("startIndex=5");
        assertThat(request.getPath()).contains("key=testApiKey");
        assertThat(books.getFirst().getVolumeInfo().getTitle()).isEqualTo("Grammaire analytique et pratique de la langue polonaise a l'usage des francais par N. Orda");
    }

    // get random books | invalid starting index of -1. Call cannot succeed
    @Test
    void getRandomBooks_shouldReturnErrorInvalidValueAtStartingIndex() throws IOException {

        // Given
        String response = TestUtils.loadJsonFromResource(
                "jsonResponses/getRandomBooks/getRandomBooksStartingIndex=-1.json"
        );

        // When
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .addHeader("Content-Type", "application/json")
                .setBody(response));

        // Then
        assertThatThrownBy(() -> service.getRandomBooks())
                .isInstanceOf(HttpClientErrorException.class)
                .satisfies(ex -> {
                    HttpClientErrorException httpExc = (HttpClientErrorException) ex;
                    assertThat(httpExc.getStatusCode().value()).isEqualTo(400);
                    assertThat(httpExc.getResponseBodyAsString()).isEqualTo(response);
                });
    }

    // get random book | set starting index as 999, return totalItems object with value of zero, due to api limit results
    @Test
    void getRandomBooks_shouldReturnZeroTotalItems() throws IOException, InterruptedException {

        // Given
        String response = TestUtils.loadJsonFromResource(
                "jsonResponses/getRandomBooks/getRandomBooksStartingIndex999.json"
        );

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody(response)
        );

        when(indexGenerator.generateStartIndex(anyInt(), anyInt())).thenReturn(5);

        // When
        List<Book> books = service.getRandomBooks();

        // Then
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getPath()).contains("q=a");
        assertThat(recordedRequest.getPath()).contains("startIndex=5");
        assertThat(recordedRequest.getPath()).contains("key=testApiKey");

        assertThat(books).isEmpty();
    }


    // BOOK DETAIL

    // get book detail | proper call without errors
    @Test
    void getBookDetails_shouldReturnDetailsAboutBook() throws IOException, InterruptedException {

        // Given
        String response = TestUtils.loadJsonFromResource(
                "jsonResponses/getBookDetail/getBookDetailsProperCall.json"
        );

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody(response)
        );

        // When
        Book book = service.getBookDetails("testId");

        // Then
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getPath()).contains("/testId");
        assertThat(recordedRequest.getPath()).contains("key=testApiKey");
        assertThat(book).isNotNull();
        assertThat(book.getVolumeInfo().getTitle()).isEqualTo("The Google Story (2018 Updated Edition)");
    }

    // get detailed book| parameterized error scenarios
    @ParameterizedTest
    @MethodSource("com.example.bookshelf.util.TestUtils#errorScenarios")
    void getDetailedBook_parameterizedTests(
            String json,
            HttpStatus httpStatus,
            String errorMessage
    ) throws IOException {
        String response = TestUtils.loadJsonFromResource(json);

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(httpStatus.value())
                .addHeader("Content-Type", "application/json")
                .setBody(response));

        assertThatThrownBy(() -> service.getBookDetails("testId"))
                .isInstanceOf(HttpStatusCodeException.class)
                .satisfies(exc -> {
                            HttpStatusCodeException errorException = (HttpStatusCodeException) exc;
                            assertThat(errorException.getStatusCode().value()).isEqualTo(httpStatus.value());
                            assertThat(errorException.getResponseBodyAsString()).contains(errorMessage);
                        }
                );
    }

    // USER SEARCH

    // user search | parameterized tests (that includes using space in query, since it'll produce 400 missing query)
    @ParameterizedTest
    @MethodSource("com.example.bookshelf.util.TestUtils#errorScenarios")
    void userSearch_parameterizedTests(
            String filePath,
            HttpStatus httpStatus,
            String errorMessage
    ) throws IOException {
        String response = TestUtils.loadJsonFromResource(filePath);

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(httpStatus.value())
                .addHeader("Content-Type", "application/json")
                .setBody(response)
        );

        assertThatThrownBy(() ->
                service.getBooksForUserQueryQuickSearch("userSearch"))
                .isInstanceOf(HttpStatusCodeException.class)
                .satisfies(exc -> {
                    HttpStatusCodeException codeException = (HttpStatusCodeException) exc;
                    assertThat(codeException.getStatusCode().value()).isEqualTo(httpStatus.value());
                    assertThat(codeException.getResponseBodyAsString()).contains(errorMessage);
                });
    }

    // user search | try to perform a call with special characters. Api allow such query. It covers both special characters test, and proper call
    @Test
    void userSearch_shouldReturnProperCallWithSpecialCharactersInQuery() throws IOException, InterruptedException {
        // Given
        String response = TestUtils.loadJsonFromResource(
                "jsonResponses/userQuery/userQuerySpecialCharacters.json"
        );

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody(response)
        );
        String specialQuery = "c++ <script>alert('xss')</script> !@# '1' = '1";
        String encodedQuery = "c++%20%3Cscript%3Ealert('xss')%3C/script%3E%20!@%23%20'1'%20%3D%20'1";

        // When
        List<Book> books = service.getBooksForUserQueryQuickSearch(specialQuery);

        // Then
        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        //URI un service layer use .encode, check for encoded characters
        assertThat(recordedRequest.getPath()).contains("q=" + encodedQuery);
        assertThat(recordedRequest.getPath()).contains("key=testApiKey");

        assertThat(books).isNotEmpty();
        assertThat(books.getFirst().getVolumeInfo().getTitle()).isEqualTo(
                "The Code of Federal Regulations of the United States of America"
        );
    }
}