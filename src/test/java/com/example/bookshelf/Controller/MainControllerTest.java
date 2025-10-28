package com.example.bookshelf.Controller;

import com.example.bookshelf.Model.Book;
import com.example.bookshelf.Model.BooksWrapper;
import com.example.bookshelf.Service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.Files;
import java.nio.file.Path;
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


    // get random books | prepare a proper call for getting random books
    @Test
    void shouldReturnRandomBooks() throws Exception {
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

    // get detailed book| prepare a proper call for getting details about chosen book
    @Test
    void shouldReturnDetailsAboutBook(){

    }


//        @RequestMapping(value = "/makeQuery", method = RequestMethod.GET)
//    public String makeCallByUserSearch(@RequestParam(name = "userData") String userQuery, Model model){
//        model.addAttribute("userQuery", service.getBooksForUserQuery(userQuery));
//        return "userQuery";
//    }
}