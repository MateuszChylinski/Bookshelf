package com.example.bookshelf.controller;

import com.example.bookshelf.model.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class UserDashboardController {

    @GetMapping("/myaccount")
    public String prepareUser(Model model) {

        Authentication authenticator = SecurityContextHolder.getContext().getAuthentication();

        if (authenticator != null && authenticator.isAuthenticated()) {

            User user = new User();
            user.setUsername(authenticator.getName());
            model.addAttribute("loggedInUser", user);
        }
        return "userDashboard";
    }
}
