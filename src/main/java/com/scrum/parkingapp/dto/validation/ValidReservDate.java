package com.scrum.parkingapp.dto.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BirthDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidReservDate {

    String message() default "Reservation Date must be between today and 6 weeks from today";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


}
