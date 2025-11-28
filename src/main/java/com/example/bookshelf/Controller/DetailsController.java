package com.example.bookshelf.Controller;

import com.example.bookshelf.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DetailsController {

    @Autowired
    private BookService service;

    @GetMapping("/books/details/{id}")
    public String getBookDetails(
            @PathVariable("id") String bookId,
            Model model) {
        model.addAttribute("bookDetails", service.getBookDetails(bookId));
        return "/bookDetails";
    }
}