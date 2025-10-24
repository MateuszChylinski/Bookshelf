package com.example.bookshelf.Controller;

import com.example.bookshelf.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

    @Autowired
    private BookService service;

    @RequestMapping(value="/getBooks", method = RequestMethod.GET)
    public String welcome_connection(Model model){
        model.addAttribute("fictionBooks", service.getAllFictionBooks());
        return "index";
    }
}
