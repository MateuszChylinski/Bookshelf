package com.example.bookshelf.Controller;

import com.example.bookshelf.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    private BookService service;

    @RequestMapping(value = "/makeQuery", method = RequestMethod.GET)
    public String makeCallByUserSearch(@RequestParam(name = "userData") String userQuery, Model model){
        model.addAttribute("userQuery", service.getBooksForUserQuery(userQuery));
        return "userQuery";
    }

    @RequestMapping(value="/getBooks", method = RequestMethod.GET)
    public String getRandomBooks(Model model){
        model.addAttribute("randomBooks", service.getRandomBooks());
        return "index";
    }
}
