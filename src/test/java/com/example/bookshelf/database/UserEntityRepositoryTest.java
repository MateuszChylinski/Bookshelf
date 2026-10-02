package com.example.bookshelf.database;

import com.example.bookshelf.model.entities.UserEntity;
import com.example.bookshelf.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class UserEntityRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;
    private UserEntity userEntity;

    @BeforeEach
    void prepareUserObject() {
        userEntity = UserEntity.builder()
                .username("username")
                .password("password")
                .email("email")
                .build();
    }

    @Test
    void givenNewUser_whenSave_thenSuccess() {
        UserEntity insertedUserEntity = userRepository.save(userEntity);
        entityManager.flush();
        entityManager.clear();

        assertThat(entityManager.find(UserEntity.class, insertedUserEntity.getUserId())).isEqualTo(
                userEntity
        );
    }

    @Test
    void givenUserCreated_whenUpdate_thenSuccess() {
        entityManager.persist(userEntity);
        entityManager.flush();
        entityManager.clear(); // clear, so the entity will be detached. dirty checking is off now.

        String newUsername = "new username";
        userEntity.setUsername(newUsername);

        userRepository.save(userEntity);
        entityManager.flush();
        entityManager.clear();

        assertThat(entityManager.find(UserEntity.class, userEntity.getUserId()).getUsername()).isEqualTo(newUsername);
    }

    @Test
    void givenUser_whenFindByUsername_thenUserIsFound() {
        entityManager.persist(userEntity);
        entityManager.flush();
        entityManager.clear();

        Optional<UserEntity> foundUser = userRepository.findByUsername(userEntity.getUsername());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo(userEntity.getUsername());
    }

    @Test
    void givenUser_whenFindByEmail_thenUserIsFound() {
        entityManager.persist(userEntity);
        entityManager.flush();
        entityManager.clear();

        Optional<UserEntity> foundUser = userRepository.findByEmail(userEntity.getEmail());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(userEntity.getEmail());
    }

    @Test
    void givenUser_whenDelete_thenUserIsGone() {
        entityManager.persist(userEntity);
        entityManager.flush();

        userRepository.delete(userEntity);
        entityManager.flush();
        entityManager.clear();

        assertThat(userRepository.findById(userEntity.getUserId())).isEmpty();
    }

    @Test
    void givenNoUser_whenFindById_thenEmpty() {
        Optional<UserEntity> user = userRepository.findById(123);

        assertThat(user).isEmpty();
    }

    @Test
    void givenNonExistentUsername_whenFindByName_thenEmpty() {
        Optional<UserEntity> user = userRepository.findByUsername("nonexistentusername");
        assertThat(user).isEmpty();
    }

    @Test
    void givenNonExistentUserEmail_whenFindByEmail_thenEmpty() {
        Optional<UserEntity> user = userRepository.findByEmail("nonexistentemail@abc.com");

        assertThat(user).isEmpty();
    }

    @Test
    void givenDuplicatedEmail_whenSave_thenThrows() {
        entityManager.persist(userEntity);
        entityManager.flush();
        entityManager.clear();

        UserEntity secondUserEntity = UserEntity.builder()
                .username("username2")
                .password("password2")
                .email("email")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(secondUserEntity);
            entityManager.flush();
            entityManager.clear();
        });
    }

    @Test
    void givenDuplicatedUsername_whenSave_thenThrows() {
        entityManager.persist(userEntity);
        entityManager.flush();
        entityManager.clear();

        UserEntity secondUserEntity = UserEntity.builder()
                .username("username")
                .password("password123")
                .email("email123")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(secondUserEntity);
            entityManager.flush();
            entityManager.clear();
        });
    }
}

