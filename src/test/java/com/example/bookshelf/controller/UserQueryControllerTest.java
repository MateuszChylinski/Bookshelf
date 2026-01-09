package com.example.bookshelf.controller;

import com.example.bookshelf.model.rest.BooksMapper;
import com.example.bookshelf.model.error.ErrorMapper;
import com.example.bookshelf.service.BookService;
import com.example.bookshelf.util.TestUtils;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application.properties")
@WebMvcTest(UserQueryController.class)
public class UserQueryControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private BookService mockService;

    // user query | prepare a call with long query. Should return 0 items
    @Test
    @WithMockUser
    void userQuery_shouldReturn0TotalItems() throws Exception {
        BooksMapper booksMapper = TestUtils.loadJson(
                "jsonResponses/userQuery/userQueryLongQuery0Items.json",
                BooksMapper.class);

        when(mockService.getBooksForUserQueryQuickSearch
                ("userQuery")).thenReturn(booksMapper.getBookItems());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/books/search")
                        .param("q", "userQuery"))
                .andExpect(status().isOk())
                .andExpect(view().name("/fragments/userQueryFragment"))
                .andExpect(model().attribute("results", booksMapper.getBookItems()));
    }

    /*
     user query | prepare a call with query parameter
     values with special characters/queries, katakana, sql like strings
     such as c++, <script>, !@#, '1'='1, tested on real api
    */
    @Test
    @WithMockUser
    void userQuery_shouldHandleSpecialCharactersCall() throws Exception {
        BooksMapper booksMapper = TestUtils.loadJson(
                "jsonResponses/userQuery/userQuerySpecialCharacters.json",
                BooksMapper.class
        );

        String specialQuery = "c++ <script>alert('xss')</script> !@# '1' = '1";

        when(mockService.getBooksForUserQueryQuickSearch(specialQuery)).thenReturn(booksMapper.getBookItems());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/books/search")
                        .param("q", specialQuery))
                .andExpect(status().isOk())
                .andExpect(view().name("/fragments/userQueryFragment"))
                .andExpect(model().attribute("results", booksMapper.getBookItems()));
    }

    // user query | parameterized tests
    @WithMockUser
    @ParameterizedTest
    @MethodSource("com.example.bookshelf.util.TestUtils#errorScenarios")
    void userQuery_parameterizedTests(
            String jsonFilePath,
            HttpStatus httpStatus,
            String errorMessage
    ) throws Exception {

        ErrorMapper errorMapper = TestUtils.loadJson(
                jsonFilePath, ErrorMapper.class
        );

        when(mockService.getBooksForUserQueryQuickSearch("userQuery"))
                .thenThrow(TestUtils.createHttpException(
                        httpStatus, errorMessage, errorMapper.getError().getMessage())
                );

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/books/search")
                        .param("q", "userQuery"))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(view().name("error"))
                .andExpect(model().attribute("globalExceptionHandlerMessage", httpStatus.value() + " " + errorMapper.getError().getMessage()));
    }

    // user query | should reject unauthorized user
    @Test
    void getUserQuery_shouldRejectUnauthorizedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/books/search")
                        .param("q", "userQuery"))
                .andExpect(status().isUnauthorized());
    }
}
