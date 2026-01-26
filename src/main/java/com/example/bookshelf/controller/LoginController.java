package com.example.bookshelf.controller;

import com.example.bookshelf.model.entities.User;
import com.example.bookshelf.model.form.UserForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String createUserObject(Model model) {
        model.addAttribute("loginUser", new User());
        return "login";
    }

    @PostMapping("/login")
    public String sendCredentials(
            @Valid @ModelAttribute UserForm userForm,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            return "error";
        }

        return "index";
    }
}
