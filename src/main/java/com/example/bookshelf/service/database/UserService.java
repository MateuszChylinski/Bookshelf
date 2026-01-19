package com.example.bookshelf.service.database;

import com.example.bookshelf.model.entities.Users;
import com.example.bookshelf.model.form.RegistrationForm;
import com.example.bookshelf.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

import jakarta.validation.Validator;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final Validator validator;
    private final UserRepository repository;

    public void registerNewAccount(RegistrationForm registrationForm) {

        Set<ConstraintViolation<RegistrationForm>> violations =
                validator.validate(registrationForm);

        if (!violations.isEmpty()) {
            StringBuilder error = new StringBuilder();

            for (ConstraintViolation<RegistrationForm> violation : violations) {
                error.append(violation.getMessage());
            }
            throw new ConstraintViolationException("Violation error occurred: " + error, violations);
        }

        Users user = Users.builder()
                .username(registrationForm.getUsername())
                .password(passwordEncoder.encode(registrationForm.getPassword()))
                .email(registrationForm.getEmail())
                .build();

        repository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));

        return User.builder()
                .username(users.getUsername())
                .password(users.getPassword())
                .roles("USER") // consider adding roles
                .build();
    }
}


//TODO check if username/email exists
