package com.scrum.parkingapp.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "Invalid password";

    int minLength() default 8;

    int maxLength() default 20;

    String allowedSpecialCharacters() default "!@#^&*()-+";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
