package com.example.bookshelf.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception{
        security
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/get/**").permitAll()
                        .anyRequest().authenticated());
        return security.build();
    }
}
