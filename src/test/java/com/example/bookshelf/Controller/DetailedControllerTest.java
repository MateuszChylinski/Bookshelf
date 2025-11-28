package com.example.bookshelf.Controller;

import com.example.bookshelf.Model.Book;
import com.example.bookshelf.Model.ErrorModels.ErrorMapper;
import com.example.bookshelf.Service.BookService;
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
@WebMvcTest(DetailsController.class)
public class DetailedControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private BookService mockService;

    // get book details | parameterize error tests
    @WithMockUser
    @ParameterizedTest
    @MethodSource("com.example.bookshelf.util.TestUtils#errorScenarios")
    void getDetailedBook_handleErrorScenarios(String json, HttpStatus httpStatus, String message) throws Exception {
        ErrorMapper errorMapper = TestUtils.loadJson(json, ErrorMapper.class);

        when(mockService.getBookDetails("id")).thenThrow(TestUtils.createHttpException(httpStatus, message, errorMapper.getError().getMessage()
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/books/details/{id}", "id"))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(view().name("/error"))
                .andExpect(model().attribute("globalExceptionHandlerMessage", httpStatus.value() + " " + errorMapper.getError().getMessage()));
    }

    // get book details | make a proper call
    @WithMockUser
    @Test
    void getDetailedBook_shouldReturnDetailedBook() throws Exception {
        Book book = TestUtils.loadJson(
                "jsonResponses/getBookDetail/getBookDetailsProperCall.json",
                Book.class);

        when(mockService.getBookDetails("testId")).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/books/details/{id}", "testId"))
                .andExpect(status().isOk())
                .andExpect(view().name("/bookDetails"))
                .andExpect(model().attribute("bookDetails", book));
    }

    // get book details | reject unauthorized user
    @Test
    void getDetailedBook_shouldRejectUnauthorizedUser() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .get("/books/details/{id}", "testId"))
                .andExpect(status().isUnauthorized());
    }
}