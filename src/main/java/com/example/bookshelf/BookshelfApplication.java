package com.example.bookshelf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookshelfApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookshelfApplication.class, args);
    }
}

// TODO make a proper error web page
// TODO improve 'statistics' in user dashboard

// TODO improve top navbar so it will display nickname of user when authenticated
// TODO add account icon and navigate to dashboard

// TODO redirect when navigating back to previous page instead of making new api call
// TODO add tests


