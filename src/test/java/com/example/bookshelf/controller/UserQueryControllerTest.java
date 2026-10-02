package com.example.bookshelf.controller;

import com.example.bookshelf.config.PasswordEncoderConfig;
import com.example.bookshelf.config.SecurityConfig;
import com.example.bookshelf.model.records.HttpError;
import com.example.bookshelf.model.rest.BooksMapper;
import com.example.bookshelf.model.error.ErrorMapper;
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

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({SecurityConfig.class, PasswordEncoderConfig.class})
@WebMvcTest(UserQueryController.class)
public class UserQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private BookRestService mockService;
    @MockitoBean
    private UserDetailsService userDetailsService;

    // user query | prepare a call with long query. Should return 0 items
    @Test
    void userQuery_shouldReturnEmptyList() throws Exception {

        String testQuery = "longQueryExample";

        when(mockService.getBooksForUserQueryQuickSearch
                (testQuery)).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/books/search")
                        .param("q", testQuery))
                .andExpect(status().isOk())
                .andExpect(view().name("/fragments/userQueryFragment"))
                .andExpect(model().attribute("results", List.of()));
    }

    /*
     user query | prepare a call with query parameter
     values with special characters/queries, katakana, sql like strings
     such as c++, <script>, !@#, '1'='1, tested on real api
    */
    @Test
    void userQuery_shouldBindAndReturnResults() throws Exception {
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
    @ParameterizedTest
    @MethodSource("com.example.bookshelf.util.TestUtils#errorScenarios")
    void userQuery_parameterizedErrorScenarios(
            String jsonFilePath,
            HttpStatus httpStatus
    ) throws Exception {

        ErrorMapper errorMapper = TestUtils.loadJson(
                jsonFilePath, ErrorMapper.class
        );
        when(mockService.getBooksForUserQueryQuickSearch("userQuery"))
                .thenThrow(TestUtils.createHttpException(
                        httpStatus, TestUtils.loadJsonFromResource(jsonFilePath)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/books/search")
                        .param("q", "userQuery"))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(view().name("error"))
                .andExpect(model().attribute("globalAdviceRequestFailed", new HttpError(httpStatus.value(), errorMapper.getError().getMessage())));
    }
}
