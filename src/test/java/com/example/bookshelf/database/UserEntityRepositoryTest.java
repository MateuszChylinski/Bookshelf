package com.example.bookshelf.database;

import com.example.bookshelf.model.entities.User;
import com.example.bookshelf.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    public UserRepository userRepository;
    @Autowired
    public TestEntityManager entityManager;
    private User user;

    @BeforeEach
    void prepareUserObject() {
        user = User.builder()
                .username("username")
                .password("password")
                .email("email")
                .build();
    }

    @Test
    void givenNewUser_whenSave_thenSuccess() {
        User insertedUser = userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        assertThat(entityManager.find(User.class, insertedUser.getUserId())).isEqualTo(
                user
        );
    }

    @Test
    void givenUserCreated_whenUpdate_thenSuccess() {
        entityManager.persist(user);
        entityManager.flush();

        String newUsername = "new username";
        user.setUsername(newUsername);
        entityManager.flush();
        entityManager.clear();


        assertThat(entityManager.find(User.class, user.getUserId()).getUsername()).isEqualTo(newUsername);
    }

    @Test
    void givenUser_whenFindByUsername_thenUserIsFound() {
        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();

        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.isPresent());
    }

    @Test
    void givenUser_whenFindByEmail_thenUserIsFound() {
        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();

        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void givenUser_whenDelete_thenUserIsGone() {
        entityManager.persist(user);
        entityManager.flush();

        userRepository.delete(user);
        entityManager.flush();
        entityManager.clear();

        assertThat(userRepository.findById(user.getUserId())).isEmpty();
    }

    @Test
    void givenNoUser_whenFindById_thenEmpty() {
        Optional<User> user = userRepository.findById(123);

        assertThat(user).isEmpty();
    }

    @Test
    void givenNonExistentUsername_whenFindByName_thenNull() {
        Optional<User> user = userRepository.findByUsername("nonexistentusername");
        assertThat(user.isEmpty());
    }

    @Test
    void givenNonExistentUserEmail_whenFindByEmail_thenEmpty() {
        Optional<User> user = userRepository.findByEmail("nonexistentemail@abc.com");

        assertThat(user).isEmpty();
    }

    @Test
    void givenDuplicatedEmail_whenSave_thenThrows() {
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        User secondUser = User.builder()
                .username("username2")
                .password("password2")
                .email("email")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(secondUser);
            entityManager.flush();
            entityManager.clear();
        });
    }

    @Test
    void givenDuplicatedUsername_whenSave_thenThrows() {
        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();

        User secondUser = User.builder()
                .username("username")
                .password("password123")
                .email("email123")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(secondUser);
            entityManager.flush();
            entityManager.clear();
        });
    }
}

