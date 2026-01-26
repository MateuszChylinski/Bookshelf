package com.example.bookshelf.annotations;

import com.example.bookshelf.validators.UniqueEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = UniqueEmailValidator.class)
@Retention(RUNTIME)
@Target({ElementType.FIELD})
public @interface UniqueEmail {
    String message() default "Given email is in use";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
