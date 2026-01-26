package com.example.bookshelf.service.database;

import com.example.bookshelf.model.entities.User;
import com.example.bookshelf.model.form.UserForm;
import com.example.bookshelf.repository.UserRepository;
import lombok.AllArgsConstructor;
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

        User user = User.builder()
                .username(userForm.getUsername())
                .password(passwordEncoder.encode(userForm.getPassword()))
                .email(userForm.getEmail())
                .build();

        repository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("USER") // consider adding roles
                .build();
    }
}
