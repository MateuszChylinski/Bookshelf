package com.example.bookshelf.exception;

import com.example.bookshelf.model.UserQuery;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(HttpStatusCodeException.class)
    public String handleHttpClientError(HttpClientErrorException httpClientErrorException, Model model, HttpServletResponse response) {
        response.setStatus(httpClientErrorException.getStatusCode().value());
        model.addAttribute("globalExceptionHandlerMessage", httpClientErrorException.getMessage());
        return "/error";
    }

    @ModelAttribute("userTopNavbarQuery")
    public UserQuery getUserInput() {
        return new UserQuery();
    }
}
