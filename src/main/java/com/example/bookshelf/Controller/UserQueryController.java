package com.example.bookshelf.Controller;

import com.example.bookshelf.Model.UserQuery;
import com.example.bookshelf.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class UserQueryController {

    @Autowired
    private BookService bookService;

    @GetMapping("/searchForBooks")
    public String requestBookDetails(@ModelAttribute("userInput") UserQuery userQuery, Model model) {
        model.addAttribute("results", bookService.getBooksForUserQueryQuickSearch(userQuery.getUserQuery()));
        return "/templates/userQuery";
    }
}
