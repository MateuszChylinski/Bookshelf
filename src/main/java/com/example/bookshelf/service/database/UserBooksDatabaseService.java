package com.example.bookshelf.service.database;

import com.example.bookshelf.exception.BookNotRemovedFromFavoritesException;
import com.example.bookshelf.model.entities.BookEntity;
import com.example.bookshelf.model.entities.Status;
import com.example.bookshelf.model.entities.UserEntity;
import com.example.bookshelf.model.entities.UserBooksEntity;
import com.example.bookshelf.model.records.UserBookDetailsRecord;
import com.example.bookshelf.model.records.UserStatistics;
import com.example.bookshelf.repository.UserBooksRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserBooksDatabaseService {

    private UserBooksRepository userBooksRepository;
    private BookDatabaseService bookService;

    public Record getUserStatistics(UserEntity userEntity) {
        PageRequest pageRequest = PageRequest.of(0, 1);

        return new UserStatistics(
                userBooksRepository.getFavoriteGenre(userEntity, Status.FINISHED, pageRequest),
                userBooksRepository.countAllBookPagesRead(userEntity, Status.FINISHED),
                userBooksRepository.getAllBooksRead(userEntity, Status.FINISHED),
                userBooksRepository.getCountOfFavoritesBooks(userEntity));
    }

    public Optional<UserBookDetailsRecord> findEntry(UserEntity userEntity, BookEntity bookEntity) {
        return userBooksRepository.findByUserEntityAndBookEntity(userEntity, bookEntity)
                .map(entry -> new UserBookDetailsRecord(
                        entry.getStatus(),
                        entry.getRating(),
                        entry.getIsFavorite()
                ));
    }

    @Transactional
    public void addOnShelfOrUpdate(UserEntity userEntity, BookEntity bookEntity, Status status, Integer rating, Boolean isFavorite) {
        BookEntity savedBookEntity = bookService.saveOrGetBook(bookEntity);
        Optional<UserBooksEntity> findUserAndBook = userBooksRepository.findByUserEntityAndBookEntity(userEntity, savedBookEntity);

        // Update existing book
        findUserAndBook.ifPresentOrElse(entity -> {
            if (status != null) entity.setStatus(status);
            if (rating != null) entity.setRating(rating);
            if (isFavorite != null) entity.setIsFavorite(isFavorite);
            userBooksRepository.save(entity);
        }, () -> {

            // Add book on the shelf
            UserBooksEntity userBooksEntity = UserBooksEntity.builder()
                    .userEntity(userEntity)
                    .bookEntity(savedBookEntity)
                    .added_at(LocalDateTime.now())
                    .status(status)
                    .rating(rating)
                    .isFavorite(isFavorite)
                    .build();
            userBooksRepository.save(userBooksEntity);
        });
    }

    @Transactional
    public void deleteFromFavorites(UserEntity userEntity, String bookId) {
        List<UserBooksEntity> toDelete = userBooksRepository.deleteByUserEntityAndBookEntity_ApiId(userEntity, bookId);

        if (toDelete.size() != 1) {
            throw new BookNotRemovedFromFavoritesException("Couldn't remove book from favorites");
        }
    }

    public List<BookEntity> getFavoriteBooks(UserEntity userEntity) {
        return userBooksRepository.getFinishedBooks(Status.FINISHED, userEntity);
    }
}