package com.example.bookshelf.controller;

import com.example.bookshelf.service.rest.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class UserQueryController {

    private final BookService bookService;

    @GetMapping("/books/search")
    public String searchForQueryBooks(@RequestParam("q") String providedQuery, Model model) {
        model.addAttribute("results", bookService.getBooksForUserQueryQuickSearch(providedQuery));
        return "/fragments/userQueryFragment";
    }
}