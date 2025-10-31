package com.example.bookshelf.Controller;

import com.example.bookshelf.Model.Book;
import com.example.bookshelf.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private BookService service;

    @RequestMapping(value = "/makeQuery", method = RequestMethod.GET)
    public String makeCallByUserSearch(@RequestParam(name = "selectedSubject", required = false, defaultValue = "any") String userQuery, Model model) {
        model.addAttribute("selectedSubject", service.getBooksForUserQuery(userQuery));
        return "userQuery";
    }

    @RequestMapping(value = "/getBooks", method = RequestMethod.GET)
    public String getRandomBooks(Model model) {
        List<Book> books = service.getRandomBooks() != null ? service.getRandomBooks() : Collections.emptyList();
        model.addAttribute("randomBooks", books);
        return "index";
    }
}
