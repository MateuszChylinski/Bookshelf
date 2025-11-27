package com.example.bookshelf.Controller;

import com.example.bookshelf.Model.Book;
import com.example.bookshelf.Model.BooksWrapper;
import com.example.bookshelf.Model.ErrorModels.ErrorMapper;
import com.example.bookshelf.Service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Resources;
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
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application.properties")
@WebMvcTest(DetailsController.class)
@AutoConfigureMockMvc
public class DetailedControllerTest {
    //TODO before/after all/each?

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private BookService mockService;
    @Autowired
    private ObjectMapper objectMapper;

    protected String loadJsonFromResource(String pathToFile) throws IOException {
        Resource resource = new ClassPathResource(pathToFile);
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    protected <T> T loadAndMapJsonFile(String pathToFile, Class<T> tClass) throws IOException {
        String json = loadJsonFromResource(pathToFile);
        return objectMapper.readValue(json, tClass);
    }


    protected HttpClientErrorException createHttpException(HttpStatus httpStatus, String message, String json) {
        return HttpClientErrorException.create(
                httpStatus, message, HttpHeaders.EMPTY,
                json.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8
        );
    }

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

    // Detailed book

    @WithMockUser
    @ParameterizedTest
    @MethodSource("errorScenarios")
    void getDetailedBook_handleErrorScenarios(String json, HttpStatus httpStatus, String message) throws Exception {
        String response = loadJsonFromResource(json);
        ErrorMapper errorMapper = objectMapper.readValue(response, ErrorMapper.class);

        when(mockService.getBookDetails("zyTCAlFPjgYC")).thenThrow(createHttpException(httpStatus, message, response
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/books/details/{id}", "zyTCAlFPjgYC"))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(view().name("error"))
                .andExpect(model().attribute("globalExceptionHandlerMessage", httpStatus.value()+ " " + errorMapper.getError().getMessage()));
    }
//proper call

    @WithMockUser
    @Test
    void getDetailedBook_shouldReturnProperCall() throws Exception {
        String response = loadJsonFromResource(
                "jsonResponses/getBookDetail/getBookDetailsProperCall.json"
        );

        Book book = objectMapper.readValue(response, Book.class);

        when(mockService.getBookDetails("testId")).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/books/details/{id}", "testId"))
                .andExpect(status().isOk())
                .andExpect(view().name("/bookDetails"))
                .andExpect(model().attribute("bookDetails", book));
    }
}

//    @Test
//    @WithMockUser
//    void getBookDetails_shouldReturnDetailedBook() throws Exception {
//        String response = loadJsonFromResource(
//                "jsonResponses/getBookDetail/getBookDetailsProperCall.json"
//        );
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        Book book = objectMapper.readValue(response, Book.class);
//
//        when(mockService.getBookDetails("zyTCAlFPjgYC")).thenReturn(book);
//
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
//                        .get("/books/details/{id}", "zyTCAlFPjgYC"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("/bookDetails"))
//                .andExpect(model().attribute("bookDetails", book))
//                .andReturn();
//
//        ModelAndView modelAndView = mvcResult.getModelAndView();
//        Assertions.assertNotNull(modelAndView);
//    }
//

// TODO ADD TO PARAMETRIZED getRandomBooks_shouldReturnInvalidStartingIndex
//getRandomBooks_shouldReturnMissingParameterQException

