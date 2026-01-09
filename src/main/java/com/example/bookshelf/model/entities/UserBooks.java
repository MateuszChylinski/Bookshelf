package com.example.bookshelf.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "UserBooks")
public class UserBooks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Books books;

    @Column(columnDefinition = "ENUM('READING', 'FINISHED', 'WANT')")
    @Enumerated(EnumType.STRING)
    private Status status;
    @Min(1)
    @Max(5)
    @Column(name = "rating")
    private Integer rating;
    @Column(name = "notes")
    private String notes;
    @Column(name = "added_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime added_at;

    public UserBooks(int id, Users users, Books books, Status status, int rating, String notes, LocalDateTime addedAt) {
        this.id = id;
        this.users = users;
        this.books = books;
        this.status = status;
        this.rating = rating;
        this.notes = notes;
        this.added_at = addedAt;
    }
}

