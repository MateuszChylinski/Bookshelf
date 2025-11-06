package com.example.bookshelf.Controller;

import com.example.bookshelf.Model.Book;
import com.example.bookshelf.Model.UserQuery;
import com.example.bookshelf.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private BookService service;

    @PostMapping(value = "/bookQuery")
    public String makeQuery(@ModelAttribute("userInput")  UserQuery userQuery, Model model) {
        model.addAttribute("query", userQuery);
        return "/templates/userQuery";
    }

    @RequestMapping(value = "/getBooks", method = RequestMethod.GET)
    public String getRandomBooks(Model model) {
        List<Book> books = service.getRandomBooks() != null ? service.getRandomBooks() : Collections.emptyList();
        model.addAttribute("userInput", new UserQuery());
        model.addAttribute("randomBooks", books);
        return "/templates/index";
    }
}
