package com.example.bookshelf.controller;

import com.example.bookshelf.service.rest.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@AllArgsConstructor
public class DetailsController {

    private final BookService service;

    @GetMapping("/books/details/{id}")
    public String getBookDetails(
            @PathVariable("id") String bookId,
            Model model) {
        model.addAttribute("bookDetails", service.getBookDetails(bookId));
        return "bookDetails";
    }
}