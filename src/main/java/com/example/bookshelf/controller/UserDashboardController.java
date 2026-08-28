package com.example.bookshelf.controller;

import com.example.bookshelf.model.entities.UserEntity;
import com.example.bookshelf.service.database.UserBooksDatabaseService;
import com.example.bookshelf.service.database.UserDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class UserDashboardController {

    private UserBooksDatabaseService userBooksDatabaseService;
    private UserDatabaseService userDatabaseService;


    @GetMapping("/myaccount")
    public String prepareUser(Model model) {

        Authentication authenticator = SecurityContextHolder.getContext().getAuthentication();

        if (authenticator != null && authenticator.isAuthenticated()) {
            UserEntity userEntity = (UserEntity) userDatabaseService.loadUserByUsername(authenticator.getName());

            model.addAttribute("statistics", userBooksDatabaseService.getUserStatistics(userEntity));
            model.addAttribute("loggedUser", userEntity);

            //TODO to delete.
            model.addAttribute("test123", userBooksDatabaseService.getFavoriteBooks(userEntity));
        }
        return "userDashboard";
    }
}
