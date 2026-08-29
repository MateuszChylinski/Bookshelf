package com.example.bookshelf.exception;

import com.example.bookshelf.model.rest.UserQuery;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(HttpStatusCodeException.class)
    public String handleHttpError(HttpStatusCodeException exception, Model model, HttpServletResponse response) {
        Map<Integer, String> errors = new HashMap<>();

        if (exception.getStatusCode().is4xxClientError()) {
            errors.put(exception.getStatusCode().value(),
                    "The request could not be processed, because of the client side error. (" + exception.getStatusText() + ")");

        } else if (exception.getStatusCode().is5xxServerError()) {
            errors.put(exception.getStatusCode().value(),
                    "The request could not be processed, because of the server side error. (" + exception.getStatusText() + ")");
        } else {
            errors.put(exception.getStatusCode().value(),
                    "The request could not be processed, because of the unknown error. (" + exception.getStatusText() + ")");
        }

        response.setStatus(exception.getStatusCode().value());
        model.addAttribute("globalAdviceRequestFailed", errors);
        return "error";
    }

    @ModelAttribute("userTopNavbarQuery")
    public UserQuery getUserInput() {
        return new UserQuery();
    }

    @ExceptionHandler(BookAlreadyInFavoritesException.class)
    public String handleAlreadyInFavorites(BookAlreadyInFavoritesException exception, Model model, HttpServletResponse response) {
        response.setStatus(HttpStatus.CONFLICT.value());
        model.addAttribute("globalExceptionHandlerMessage", exception.getMessage());
        return "error";
    }
}
