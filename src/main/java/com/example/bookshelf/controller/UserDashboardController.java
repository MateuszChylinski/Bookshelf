package com.example.bookshelf.controller;

import com.example.bookshelf.model.entities.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class UserDashboardController {

    @GetMapping("/myaccount")
    public String prepareUser(Model model) {

        Authentication authenticator = SecurityContextHolder.getContext().getAuthentication();

        if (authenticator != null && authenticator.isAuthenticated()) {

            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(authenticator.getName());
            model.addAttribute("loggedUser", userEntity);
        }
        return "userDashboard";
    }
}