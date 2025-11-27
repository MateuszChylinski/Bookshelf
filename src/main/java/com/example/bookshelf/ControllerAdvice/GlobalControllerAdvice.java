package com.example.bookshelf.ControllerAdvice;

import com.example.bookshelf.Model.UserQuery;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(HttpClientErrorException.class)
    public String handleHttpClientError(HttpClientErrorException httpClientErrorException, Model model, HttpServletResponse response) {
        response.setStatus(httpClientErrorException.getStatusCode().value());
        model.addAttribute("globalExceptionHandlerMessage", httpClientErrorException.getMessage());
        //TODO ?
//        model.addAttribute("userTopNavbarQuery", new UserQuery()); // ADD THIS LINE

        return "error";
    }

    @ModelAttribute("userTopNavbarQuery")
    public UserQuery getUserInput() {
        return new UserQuery();
    }
}
