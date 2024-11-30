package com.scrum.parkingapp.dto.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BirthDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface ValidBirthDate {
    String message() default "Birth Date must be between 1910 and today minus 13 years";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}