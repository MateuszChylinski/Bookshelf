package com.example.bookshelf.controller;

import com.example.bookshelf.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    private final BookService service;

    @Autowired
    public MainController(BookService service) {
        this.service = service;
    }

    @RequestMapping(value = "/getBooks", method = RequestMethod.GET)
    public String getRandomBooks(Model model) {
        model.addAttribute("randomBooks", service.getRandomBooks());
        return "/index";
    }
}
