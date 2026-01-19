package com.example.bookshelf.model.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RegistrationForm {
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 5, max = 15, message = "Username needs to be 5-15 characters")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,20}$",
            message = "Password must contain at least 8 up to 20 characters, including upper/lower case, number and special character")
    private String password;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please provide a valid email address")
    @Size(max = 254, message = "Email is too long")
    private String email;
}

