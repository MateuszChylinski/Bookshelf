package com.example.bookshelf.controller;

import com.example.bookshelf.config.PasswordEncoderConfig;
import com.example.bookshelf.config.SecurityConfig;
import com.example.bookshelf.model.records.HttpError;
import com.example.bookshelf.model.rest.Book;
import com.example.bookshelf.model.error.ErrorMapper;
import com.example.bookshelf.service.database.UserBooksDatabaseService;
import com.example.bookshelf.service.rest.BookRestService;
import com.example.bookshelf.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({SecurityConfig.class, PasswordEncoderConfig.class})
@WebMvcTest(DetailsController.class)
public class DetailedControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserBooksDatabaseService userBooksDatabaseService;
    @MockitoBean
    private BookRestService mockService;
    @MockitoBean
    private UserDetailsService userDetailsService;

    //TODO check if user is loggedi n

    // get book details | parameterize error tests
    @ParameterizedTest
    @MethodSource("com.example.bookshelf.util.TestUtils#errorScenarios")
    void getDetailedBook_handleErrorScenarios(String json, HttpStatus httpStatus) throws Exception {
        ErrorMapper errorMapper = TestUtils.loadJson(json, ErrorMapper.class);

        when(mockService.getBookDetails("testId")).thenThrow(
                TestUtils.createHttpException(httpStatus, TestUtils.loadJsonFromResource(json)));

        mockMvc.perform(MockMvcRequestBuilders.get("/books/details/{testId}", "testId"))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(view().name("error"))
                .andExpect(model().attribute("globalAdviceRequestFailed",
                        new HttpError(httpStatus.value(), errorMapper.getError().getMessage())));
    }

    // get book details | make a proper call
    @Test
    void getDetailedBook_shouldReturnDetailedBook() throws Exception {
        Book book = TestUtils.loadJson(
                "jsonResponses/getBookDetail/getBookDetailsProperCall.json",
                Book.class);

        when(mockService.getBookDetails("testId")).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/books/details/{testId}", "testId"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookDetails"))
                .andExpect(model().attribute("bookDetails", book));
    }
}