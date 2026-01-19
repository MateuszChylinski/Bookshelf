package com.example.bookshelf.controller;

import com.example.bookshelf.model.entities.Users;
import com.example.bookshelf.model.form.RegistrationForm;
import com.example.bookshelf.repository.UserRepository;
import com.example.bookshelf.service.database.UserService;
import com.example.bookshelf.service.rest.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class RegisterController {

    private final UserService userService;

    @GetMapping("/register")
    public String createUserObject(Model model) {
        model.addAttribute("user", new Users());
        return "register";
    }

    @PostMapping("/register")
    public String createNewAccount(
            @Valid @ModelAttribute RegistrationForm registrationForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "index";
        }
        userService.registerNewAccount(registrationForm);
        return "login";
    }
}
