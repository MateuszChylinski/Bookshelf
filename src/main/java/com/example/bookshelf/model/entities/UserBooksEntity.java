package com.example.bookshelf.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "UserBooks")
public class UserBooksEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity bookEntity;
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
}

