package com.example.bookshelf.Controller;

import com.example.bookshelf.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DetailsController {

    @Autowired
    private BookService service;

    @GetMapping("/get/details{bookId}")
    public String requestBookDetails(@RequestParam(value = "bookId") String bookId){
        return "bookDetails";
    }
}
