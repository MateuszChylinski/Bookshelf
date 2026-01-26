package com.example.bookshelf.controller;

import com.example.bookshelf.model.entities.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class UserConsoleController {

    @GetMapping("/myaccount")
    public String prepareUser(Model model) {

        Authentication authenticator = SecurityContextHolder.getContext().getAuthentication();

        if (authenticator != null && authenticator.isAuthenticated()) {

            Users user = new Users();
            user.setUsername(authenticator.getName());
            model.addAttribute("loggedInUser", user);
        }
        return "userConsole";
    }

}
