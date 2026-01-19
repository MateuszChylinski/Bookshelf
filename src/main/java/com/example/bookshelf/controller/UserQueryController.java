package com.example.bookshelf.controller;

import com.example.bookshelf.service.rest.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserQueryController {

    private final BookService bookService;

    @Autowired
    public UserQueryController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books/search")
    public String searchForQueryBooks(@RequestParam("q") String providedQuery, Model model) {
        model.addAttribute("results", bookService.getBooksForUserQueryQuickSearch(providedQuery));
        return "/fragments/userQueryFragment";
    }
}