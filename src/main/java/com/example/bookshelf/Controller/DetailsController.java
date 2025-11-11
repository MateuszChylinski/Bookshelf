package com.example.bookshelf.Controller;

import com.example.bookshelf.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class DetailsController {

    @Autowired
    private BookService service;

}
