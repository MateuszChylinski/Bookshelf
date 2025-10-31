package com.example.bookshelf.Controller;

import com.example.bookshelf.Model.Book;
import com.example.bookshelf.Model.BooksWrapper;
import com.example.bookshelf.Model.ErrorModels.Error;
import com.example.bookshelf.Model.ErrorModels.ErrorMapper;
import com.example.bookshelf.Service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private BookService mockService;

    // get random books | proper call for getting random books
    @Test
    void getRandomBooks_shouldReturnRandomBooks() throws Exception {
        String response = Files.readString(Path.of("src/main/resources/JsonResponses/getBooks/getBooksByCategoryProperCall"));

        ObjectMapper objectMapper = new ObjectMapper();
        BooksWrapper booksWrapper = objectMapper.readValue(response, BooksWrapper.class);
        List<Book> books = booksWrapper.getBookItems();

        when(mockService.getRandomBooks()).thenReturn(books);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/getBooks"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("randomBooks", books))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();

        Assertions.assertNotNull(modelAndView);
        ModelAndViewAssert.assertViewName(modelAndView, "index");
    }

    // get random books | make a call with wrong api key
    @Test
    void getRandomBooks_shouldReturnApiKeyNotValidException() throws Exception {
        String response = Files.readString(Path.of("src/main/resources/JsonResponses/global/getBooksBadApiKey"));

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorMapper errorMapper = objectMapper.readValue(response, ErrorMapper.class);
        Error error = errorMapper.getError();

        HttpClientErrorException invalidApiKeyException = HttpClientErrorException.create(
                HttpStatus.BAD_REQUEST, "API key not valid. Please pass a valid API key.",
                HttpHeaders.EMPTY,
                response.getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8);

        when(mockService.getRandomBooks()).thenThrow(invalidApiKeyException);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getBooks"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("globalExceptionHandlerMessage", "400 " + error.getMessage()))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        Assertions.assertNotNull(modelAndView);
        ModelAndViewAssert.assertViewName(modelAndView, "error");
    }

    // get random books | prepare a call with missing parameter q
    @Test
    void getRandomBooks_shouldReturnMissingParameterQException() throws Exception {
        String response = Files.readString(Path.of("src/main/resources/JsonResponses/getBooks/getBooksMissingParameter"));

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorMapper errorMapper = objectMapper.readValue(response, ErrorMapper.class);
        Error error = errorMapper.getError();

        HttpClientErrorException missingParameterQException = HttpClientErrorException.create(
                HttpStatus.BAD_REQUEST, "Required parameter: q",
                HttpHeaders.EMPTY,
                response.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);

        when(mockService.getRandomBooks()).thenThrow(missingParameterQException);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getBooks"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("globalExceptionHandlerMessage", "400 " + error.getMessage()))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        Assertions.assertNotNull(modelAndView);
        ModelAndViewAssert.assertViewName(modelAndView, "error");
    }

    // get random books | prepare a call with missing query
    @Test
    void getRandomBooks_shouldReturnMissingQException() throws Exception {
        String response = Files.readString(Path.of("src/main/resources/JsonResponses/getBooks/getBooksMissingQuery"));

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorMapper errorMapper = objectMapper.readValue(response, ErrorMapper.class);
        Error error = errorMapper.getError();

        HttpClientErrorException missingParameterException = HttpClientErrorException.create(
                HttpStatus.BAD_REQUEST, "Missing query.",
                HttpHeaders.EMPTY,
                response.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8
        );

        when(mockService.getRandomBooks()).thenThrow(missingParameterException);

        MvcResult requestBuilders = mockMvc.perform(MockMvcRequestBuilders.get("/getBooks"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("globalExceptionHandlerMessage", "400 " + error.getMessage()))
                .andReturn();

        ModelAndView modelAndView = requestBuilders.getModelAndView();

        Assertions.assertNotNull(modelAndView);
        ModelAndViewAssert.assertViewName(modelAndView, "error");

    }

    // get random books | prepare a call with starting index which is not supported
    @Test
    void getRandomBooks_shouldReturnEmptyList() throws Exception {
        when(mockService.getRandomBooks()).thenReturn(Collections.emptyList());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/getBooks"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("randomBooks", Collections.emptyList()))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        Assertions.assertNotNull(modelAndView);
        ModelAndViewAssert.assertViewName(modelAndView, "index");
    }

    // get random books | prepare a call with negative starting index
    @Test
    void getRandomBooks_shouldReturnInvalidStartingIndex() throws Exception {
        String response = Files.readString(Path.of("src/main/resources/JsonResponses/getBooks/getBooksStartingIndex=-1"));

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorMapper errorMapper = objectMapper.readValue(response, ErrorMapper.class);
        Error error = errorMapper.getError();

        HttpClientErrorException invalidStartingIndex = HttpClientErrorException.create(
                HttpStatus.BAD_REQUEST, "Invalid value at 'start_index' (TYPE_UINT32), \"-1\"",
                HttpHeaders.EMPTY,
                response.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8
        );

        when(mockService.getRandomBooks()).thenThrow(invalidStartingIndex);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getBooks"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("globalExceptionHandlerMessage", "400 " + error.getMessage()))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        Assertions.assertNotNull(modelAndView);
        ModelAndViewAssert.assertViewName(modelAndView, "error");
    }
}