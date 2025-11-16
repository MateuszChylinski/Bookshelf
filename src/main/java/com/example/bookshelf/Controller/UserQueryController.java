package com.example.bookshelf.Controller;

import com.example.bookshelf.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserQueryController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books/search")
    public String searchForQueryBooks(@RequestParam String userQuery, Model model) {
        model.addAttribute("results", bookService.getBooksForUserQueryQuickSearch(userQuery));
        return "/fragments/userQueryFragment";
    }
}