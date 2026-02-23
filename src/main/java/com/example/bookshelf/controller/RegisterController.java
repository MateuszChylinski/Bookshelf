package com.example.bookshelf.controller;

import com.example.bookshelf.model.form.UserForm;
import com.example.bookshelf.service.database.UserDatabaseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class RegisterController {

    private final UserDatabaseService userDatabaseService;

    @GetMapping("/register")
    public String createUserObject(Model model) {
        model.addAttribute("registerUser", new UserForm());
        return "register";
    }

    @PostMapping("/register")
    public String createNewAccount(
            @Valid @ModelAttribute UserForm userForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "error";
        }
        userDatabaseService.registerNewAccount(userForm);
        return "redirect:/login";
    }
}

