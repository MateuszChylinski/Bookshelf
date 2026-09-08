package com.example.bookshelf.controller;

import com.example.bookshelf.model.entities.BookEntity;
import com.example.bookshelf.model.entities.Status;
import com.example.bookshelf.model.entities.UserEntity;
import com.example.bookshelf.model.rest.Book;
import com.example.bookshelf.repository.BooksRepository;
import com.example.bookshelf.service.database.UserBooksDatabaseService;
import com.example.bookshelf.service.rest.BookRestService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class DetailsController {

    private final BookRestService service;
    private final UserBooksDatabaseService userBooksDatabaseService;
    private final BooksRepository booksRepository;

    @PostMapping("/books/details/{id}/note")
    public String saveNewNote(
            @PathVariable("id") String bookId,
            @AuthenticationPrincipal UserEntity userEntity,
            @RequestParam String note) {

        userBooksDatabaseService.updateUserNote(userEntity, bookId, note);
        return "redirect:/books/details/" + bookId;
    }

    @GetMapping("/books/details/{id}")
    public String getBookDetails(
            @PathVariable("id") String bookId,
            @AuthenticationPrincipal UserEntity userEntity,
            Model model) {

        model.addAttribute("bookDetails", service.getBookDetails(bookId));
        model.addAttribute("bookStatus", Status.values());
        model.addAttribute(
                "shelfBookData", userBooksDatabaseService.recordTest
                        (userEntity, bookId));

        return "bookDetails";
    }

    @PostMapping("/favorites")
    public ResponseEntity<String> addBookToFavorites(
            @RequestBody Book book,
            @RequestParam(value = "rating", required = false) Integer rating,
            @RequestParam(value = "status", required = false) Status bookStatus,
            @RequestParam(value = "isFavorite", required = false) Boolean isFavorite,
            @AuthenticationPrincipal UserEntity userEntity) {

        BookEntity mappedBook = Book.mapToEntity(book);

        userBooksDatabaseService.addOnShelfOrUpdate(userEntity, mappedBook, bookStatus, rating, isFavorite);
        return ResponseEntity.status(HttpStatus.CREATED).body("Book added to favorites");
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<String> deleteFromFavorites(
            @PathVariable("id") String bookId,
            @AuthenticationPrincipal UserEntity userEntity) {
        userBooksDatabaseService.deleteFromFavorites(userEntity, bookId);
        return ResponseEntity.status(HttpStatus.OK).body("Book has been removed from favorites");
    }
}

