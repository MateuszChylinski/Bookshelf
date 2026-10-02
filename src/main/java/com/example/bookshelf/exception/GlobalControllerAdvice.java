package com.example.bookshelf.exception;

import com.example.bookshelf.model.error.ErrorMapper;
import com.example.bookshelf.model.records.HttpError;
import com.example.bookshelf.model.rest.UserQuery;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.HttpStatusCodeException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@AllArgsConstructor
@ControllerAdvice
public class GlobalControllerAdvice {

    private final JsonMapper jsonMapper;

    @ExceptionHandler(BookNotOnTheShelfException.class)
    public String handleBookNotOnTheShelfException(BookNotOnTheShelfException exception, Model model, HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("globalExceptionHandlerMessage", exception.getMessage());
        return "error";
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public String handleHttpError(HttpStatusCodeException exception, Model model, HttpServletResponse response) {

        String errorMessage = exception.getStatusText();

        try {
            ErrorMapper errorMapper = jsonMapper.readValue(exception.getResponseBodyAsString(), ErrorMapper.class);

            if (errorMapper != null && errorMapper.getError() != null) {
                errorMessage = errorMapper.getError().getMessage();
            }
        } catch (JacksonException e) {
            // errorMessage will stay as it is, as a fallback.
        }
        HttpError error = new HttpError(exception.getStatusCode().value(),
                errorMessage);
        response.setStatus(exception.getStatusCode().value());
        model.addAttribute("globalAdviceRequestFailed", error);

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
