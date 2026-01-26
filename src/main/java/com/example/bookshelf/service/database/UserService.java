package com.example.bookshelf.service.database;

import com.example.bookshelf.model.entities.Users;
import com.example.bookshelf.model.form.UserForm;
import com.example.bookshelf.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public void registerNewAccount(UserForm userForm) {

        Users user = Users.builder()
                .username(userForm.getUsername())
                .password(passwordEncoder.encode(userForm.getPassword()))
                .email(userForm.getEmail())
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
