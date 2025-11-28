package com.example.bookshelf.controller;

import com.example.bookshelf.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DetailsController {

    private final BookService service;

    @Autowired
    public DetailsController(BookService service) {
        this.service = service;
    }

    @GetMapping("/books/details/{id}")
    public String getBookDetails(
            @PathVariable("id") String bookId,
            Model model) {
        model.addAttribute("bookDetails", service.getBookDetails(bookId));
        return "/bookDetails";
    }
}