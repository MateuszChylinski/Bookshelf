package com.example.bookshelf.database;

import com.example.bookshelf.model.entities.UserEntity;
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
public class UserEntityRepositoryTest {

    @Autowired
    public UserRepository userRepository;
    @Autowired
    public TestEntityManager entityManager;
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

        String newUsername = "new username";
        userEntity.setUsername(newUsername);
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

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.isPresent());
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
    void givenNonExistentUsername_whenFindByName_thenNull() {
        Optional<UserEntity> user = userRepository.findByUsername("nonexistentusername");
        assertThat(user.isEmpty());
    }

    @Test
    void givenNonExistentUserEmail_whenFindByEmail_thenEmpty() {
        Optional<UserEntity> user = userRepository.findByEmail("nonexistentemail@abc.com");

        assertThat(user).isEmpty();
    }

    @Test
    void givenDuplicatedEmail_whenSave_thenThrows() {
        userRepository.save(userEntity);
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

