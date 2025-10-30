package com.example.bookshelf.ControllerAdvice;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class ControllerGlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public String handleHttpClientError(HttpClientErrorException httpClientErrorException, Model model, HttpServletResponse response) {
        response.setStatus(httpClientErrorException.getStatusCode().value());
        model.addAttribute("globalExceptionHandlerMessage", httpClientErrorException.getMessage());
        return "error";
    }
}
