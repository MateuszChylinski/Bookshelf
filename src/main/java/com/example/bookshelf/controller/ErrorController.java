package com.example.bookshelf.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class ErrorController {

    @GetMapping("/error")
    public String renderErrorPage(HttpServletRequest request, Model model) {
        Object o = request.getAttribute("customError");

        if (o instanceof Map<?, ?> error) {
            model.addAttribute("customError", error);
        } else {
            model.addAttribute("customError", Map.of("Exception", "Unknown Error"));
        }
        return "error";
    }
}
