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

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({SecurityConfig.class, PasswordEncoderConfig.class})
@WebMvcTest(MainController.class)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private BookRestService mockService;
    @MockitoBean
    private UserDetailsService userDetailsService;

    // get random books | proper call for getting random books
    @Test
    void getRandomBooks_shouldReturnRandomBooks() throws Exception {
        BooksMapper booksMapper = TestUtils.loadJson(
                "jsonResponses/getRandomBooks/getRandomBooksByCategoryProperCall.json",
                BooksMapper.class
        );

        when(mockService.getRandomBooks()).thenReturn(booksMapper.getBookItems());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/getBooks"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("randomBooks", booksMapper.getBookItems()));
    }

    //     get random books | parameterized error scenarios
    @ParameterizedTest
    @MethodSource("com.example.bookshelf.util.TestUtils#errorScenarios")
    void getRandomBooks_handleErrorScenarios(
            String jsonFile,
            HttpStatus httpStatus
    ) throws Exception {
        ErrorMapper errorMapper = TestUtils.loadJson(jsonFile, ErrorMapper.class);

        when(mockService.getRandomBooks()).thenThrow(TestUtils.createHttpException(httpStatus, TestUtils.loadJsonFromResource(jsonFile)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/getBooks"))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(view().name("error"))
                .andExpect(model().attribute("globalAdviceRequestFailed",
                        new HttpError(httpStatus.value(), errorMapper.getError().getMessage())));
    }

    // get random books | prepare a call that will return an empty list
    @Test
    void getRandomBooks_shouldReturnEmptyList() throws Exception {
        when(mockService.getRandomBooks()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/getBooks"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("randomBooks", Collections.emptyList()));
    }
}