package com.example.bookshelf.Controller;

import com.example.bookshelf.Model.BooksWrapper;
import com.example.bookshelf.Model.ErrorModels.ErrorMapper;
import com.example.bookshelf.Service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application.properties")
@WebMvcTest(MainController.class)
@AutoConfigureMockMvc
public class MainControllerTest {
    //TODO before/after all/each?

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private BookService mockService;
    @Autowired
    private ObjectMapper objectMapper;

    protected String loadJsonFromResource(String path) throws IOException {
        Resource resource = new ClassPathResource(path);
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    //todo how does it work
    protected <T> T loadJson(String path, Class<T> tClass) throws IOException {
        String json = loadJsonFromResource(path);
        return objectMapper.readValue(json, tClass);
    }

    protected HttpClientErrorException createHttpException(
            HttpStatus status, String message, String json) {
        return HttpClientErrorException.create(
                status, message, HttpHeaders.EMPTY,
                json.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8
        );
    }

    //todo how does it works
    static Stream<Arguments> errorScenarios() {
        return Stream.of(
                Arguments.of("jsonResponses/global/globalBadApiKey.json",
                        HttpStatus.BAD_REQUEST,
                        "API key not valid. Please pass a valid API key."),

                Arguments.of("jsonResponses/global/globalRateLimitExceededExample.json",
                        HttpStatus.TOO_MANY_REQUESTS, "Rate Limit Exceeded"),

                Arguments.of("jsonResponses/global/globalMissingQuery.json",
                        HttpStatus.BAD_REQUEST, "Missing query."),
                Arguments.of(
                        "jsonResponses/global/globalRequiredParameterQ.json",
                        HttpStatus.BAD_REQUEST, "Required parameter: q"
                ),
                Arguments.of(
                        "jsonResponses/global/globalServiceTemporarilyUnavailable.json",
                        HttpStatus.SERVICE_UNAVAILABLE, "Service temporarily unavailable.")
        );
    }

    // get random books | proper call for getting random books
    @Test
    @WithMockUser
    public void getRandomBooks_shouldReturnRandomBooks() throws Exception {
        BooksWrapper booksWrapper = loadJson(
                "jsonResponses/getRandomBooks/getRandomBooksByCategoryProperCall.json",
                BooksWrapper.class
        );

        when(mockService.getRandomBooks()).thenReturn(booksWrapper.getBookItems());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/getBooks"))
                .andExpect(status().isOk())
                .andExpect(view().name("/index"))
                .andExpect(model().attribute("randomBooks", booksWrapper.getBookItems()));
    }

    // get random books | parameterized test for: wrong api key / rate limit exceeded / missing q parameter
    @WithMockUser
    @ParameterizedTest
    @MethodSource("errorScenarios")
    void getRandomBooks_handleErrorScenarios(
            String jsonFile,
            HttpStatus httpStatus,
            String errorMessage
    ) throws Exception {
        String response = loadJsonFromResource(jsonFile);
        ErrorMapper errorMapper = objectMapper.readValue(response, ErrorMapper.class);

        when(mockService.getRandomBooks()).thenThrow(createHttpException(httpStatus, errorMessage, response));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/getBooks"))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(view().name("error"))
                .andExpect(model().attribute("globalExceptionHandlerMessage", httpStatus.value() + " " + errorMapper.getError().getMessage()));
    }

    // get random books | prepare a call with negative starting index
    @Test
    @WithMockUser
    public void getRandomBooks_shouldReturnInvalidStartingIndex() throws Exception {
        String response = loadJsonFromResource(
                "jsonResponses/getRandomBooks/getRandomBooksStartingIndex=-1.json"
        );

        ErrorMapper errorMapper = objectMapper.readValue(response, ErrorMapper.class);

        HttpClientErrorException invalidStartingIndex = createHttpException(
                HttpStatus.BAD_REQUEST, "Invalid value at 'start_index' (TYPE_UINT32), \"-1\"",
                response
        );

        when(mockService.getRandomBooks()).thenThrow(invalidStartingIndex);

        mockMvc.perform(MockMvcRequestBuilders.get("/getBooks"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("globalExceptionHandlerMessage", invalidStartingIndex.getStatusCode().value() + " " + errorMapper.getError().getMessage()));
    }

    // get random books | prepare a call with missing parameter q
    @Test
    @WithMockUser
    public void getRandomBooks_shouldReturnMissingParameterQException() throws Exception {
        String response = loadJsonFromResource(
                "jsonResponses/getRandomBooks/getRandomBooksMissingParameter.json"
        );

        ErrorMapper errorMapper = objectMapper.readValue(response, ErrorMapper.class);

        HttpClientErrorException missingParameterQException = createHttpException(
                HttpStatus.BAD_REQUEST, "Required parameter: q",
                response
        );

        when(mockService.getRandomBooks()).thenThrow(missingParameterQException);

        mockMvc.perform(MockMvcRequestBuilders.get("/getBooks"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("globalExceptionHandlerMessage", missingParameterQException.getStatusCode().value() + " " + errorMapper.getError().getMessage()));
    }


    // get random books | prepare a call with starting index which is not supported
    @Test
    @WithMockUser
    public void getRandomBooks_shouldReturnEmptyList() throws Exception {
        when(mockService.getRandomBooks()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/getBooks"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/index"))
                .andExpect(model().attribute("randomBooks", Collections.emptyList()));
    }

    @Test
    @WithMockUser
        // get random books | prepare a call with max results parameter set to 0,
        // it'll still return 10. Tested on real api.
    void getRandomBooks_shouldReturn10VolumesWhenMaxResultIs0() throws Exception {
        String response = loadJsonFromResource(
                "jsonResponses/getRandomBooks/getRandomBooksMaxResults0.json"
        );
        BooksWrapper booksWrapper = objectMapper.readValue(response, BooksWrapper.class);

        when(mockService.getRandomBooks()).thenReturn(booksWrapper.getBookItems());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/getBooks"))
                .andExpect(status().isOk())
                .andExpect(view().name("/index"))
                .andExpect(model().attribute("randomBooks", booksWrapper.getBookItems()));
    }

    // get random books | prepare a call with long query. Should return 0 items
    @Test
    @WithMockUser
    void getRandomBook_shouldReturn0TotalItems() throws Exception {
        String response = loadJsonFromResource(
                "jsonResponses/getRandomBooks/getRandomBooks_longQuery0Items.json"
        );

        BooksWrapper booksWrapper = objectMapper.readValue(response, BooksWrapper.class);

        when(mockService.getRandomBooks()).thenReturn(booksWrapper.getBookItems());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/getBooks"))
                .andExpect(status().isOk())
                .andExpect(view().name("/index"))
                .andExpect(model().attribute("randomBooks", Matchers.empty()));
    }

    // get random books | prepare a call with unsupported starting index parameter
    @Test
    @WithMockUser
    void getRandomBooks_shouldReturn0TotalItemsStartingIndexTooHigh() throws Exception {
        String response = loadJsonFromResource(
                "jsonResponses/getRandomBooks/getRandomBooksStartingIndex999.json"
        );
        BooksWrapper booksWrapper = objectMapper.readValue(response, BooksWrapper.class);

        when(mockService.getRandomBooks()).thenReturn(booksWrapper.getBookItems());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/getBooks"))
                .andExpect(status().isOk())
                .andExpect(view().name("/index"))
                .andExpect(model().attribute("randomBooks", Matchers.empty()));
    }

    @Test
    @WithMockUser
        // get random books | prepare a call with query parameter
        // values with special characters/queries, katakana, sql like strings
        // such as c++, <script>, !@#, '1'='1, tested on real api
    void getRandomBooks_shouldHandleSpecialCharactersCall() throws Exception {
        String response = loadJsonFromResource(
                "jsonResponses/getRandomBooks/getRandomBooksByCategoryProperCall.json"
        );

        BooksWrapper booksWrapper = objectMapper.readValue(response, BooksWrapper.class);

        when(mockService.getRandomBooks()).thenReturn(booksWrapper.getBookItems());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/getBooks"))
                .andExpect(status().isOk())
                .andExpect(view().name("/index"))
                .andExpect(model().attribute("randomBooks", booksWrapper.getBookItems()));
    }
}