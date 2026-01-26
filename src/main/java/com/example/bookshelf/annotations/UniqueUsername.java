package com.example.bookshelf.annotations;

import com.example.bookshelf.validators.UniqueUsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = UniqueUsernameValidator.class)
@Retention(RUNTIME)
@Target({ElementType.FIELD})
public @interface UniqueUsername {
    public String message() default "Username is already taken";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
