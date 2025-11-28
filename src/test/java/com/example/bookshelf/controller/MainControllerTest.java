package com.example.bookshelf.controller;

import com.example.bookshelf.model.BooksMapper;
import com.example.bookshelf.model.error.ErrorMapper;
import com.example.bookshelf.service.BookService;
import com.example.bookshelf.util.TestUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application.properties")
@WebMvcTest(MainController.class)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private BookService mockService;

    // get random books | proper call for getting random books
    @Test
    @WithMockUser
    public void getRandomBooks_shouldReturnRandomBooks() throws Exception {
        BooksMapper booksMapper = TestUtils.loadJson(
                "jsonResponses/getRandomBooks/getRandomBooksByCategoryProperCall.json",
                BooksMapper.class
        );

        when(mockService.getRandomBooks()).thenReturn(booksMapper.getBookItems());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/getBooks"))
                .andExpect(status().isOk())
                .andExpect(view().name("/index"))
                .andExpect(model().attribute("randomBooks", booksMapper.getBookItems()));
    }

    // get random books | parameterized test for: wrong api key / rate limit exceeded / missing q parameter
    @WithMockUser
    @ParameterizedTest
    @MethodSource("com.example.bookshelf.util.TestUtils#errorScenarios")
    void getRandomBooks_handleErrorScenarios(
            String jsonFile,
            HttpStatus httpStatus,
            String errorMessage
    ) throws Exception {
        ErrorMapper errorMapper = TestUtils.loadJson(jsonFile, ErrorMapper.class);

        when(mockService.getRandomBooks()).thenThrow(TestUtils.createHttpException(httpStatus, errorMessage, errorMapper.getError().getMessage()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/getBooks"))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(view().name("/error"))
                .andExpect(model().attribute("globalExceptionHandlerMessage", httpStatus.value() + " " + errorMapper.getError().getMessage()));
    }

    // get random books | prepare a call with negative starting index
    @Test
    @WithMockUser
    public void getRandomBooks_shouldReturnInvalidStartingIndex() throws Exception {
        ErrorMapper errorMapper = TestUtils.loadJson(
                "jsonResponses/getRandomBooks/getRandomBooksStartingIndex=-1.json",
                ErrorMapper.class
        );

        HttpClientErrorException invalidStartingIndex = TestUtils.createHttpException(
                HttpStatus.BAD_REQUEST, "Invalid value at 'start_index' (TYPE_UINT32), \"-1\"",
                errorMapper.getError().getMessage()
        );

        when(mockService.getRandomBooks()).thenThrow(invalidStartingIndex);

        mockMvc.perform(MockMvcRequestBuilders.get("/getBooks"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("/error"))
                .andExpect(model().attribute("globalExceptionHandlerMessage", invalidStartingIndex.getStatusCode().value() + " " + errorMapper.getError().getMessage()));
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
        BooksMapper booksMapper = TestUtils.loadJson(
                "jsonResponses/getRandomBooks/getRandomBooksMaxResults0.json",
                BooksMapper.class
        );

        when(mockService.getRandomBooks()).thenReturn(booksMapper.getBookItems());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/getBooks"))
                .andExpect(status().isOk())
                .andExpect(view().name("/index"))
                .andExpect(model().attribute("randomBooks", booksMapper.getBookItems()));
    }

    // get random books | prepare a call with unsupported starting index parameter
    @Test
    @WithMockUser
    void getRandomBooks_shouldReturn0TotalItemsStartingIndexTooHigh() throws Exception {
        BooksMapper booksMapper = TestUtils.loadJson(
                "jsonResponses/getRandomBooks/getRandomBooksStartingIndex999.json",
                BooksMapper.class);

        when(mockService.getRandomBooks()).thenReturn(booksMapper.getBookItems());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/getBooks"))
                .andExpect(status().isOk())
                .andExpect(view().name("/index"))
                .andExpect(model().attribute("randomBooks", booksMapper.getBookItems()));
    }

    // get random books | reject unauthorized user
    @Test
    void getRandomBooks_shouldRejectUnauthorizedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/getBooks"))
                .andExpect(status().isUnauthorized());
    }
    // TODO add test which will redirect user to login page
}