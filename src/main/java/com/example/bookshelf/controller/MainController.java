package com.example.bookshelf.controller;

import com.example.bookshelf.service.rest.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class MainController {

    private final BookService service;

    @GetMapping(value = "/getBooks")
    public String getRandomBooks(Model model) {
        model.addAttribute("randomBooks", service.getRandomBooks());
        return "index";
    }
}
