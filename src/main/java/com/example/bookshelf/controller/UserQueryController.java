package com.example.bookshelf.controller;

import com.example.bookshelf.service.rest.BookRestService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class UserQueryController {

    private final BookRestService bookRestService;

    @PostMapping("/books/search")
    public String searchForQueryBooks(@RequestParam("q") String providedQuery, Model model) {

        model.addAttribute("results", bookRestService.getBooksForUserQueryQuickSearch(providedQuery));
        return "/fragments/userQueryFragment";
    }
}