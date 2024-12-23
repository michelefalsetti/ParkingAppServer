package com.scrum.parkingapp.dto.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class ReservationDateValidator implements ConstraintValidator<ValidReservationDate, LocalDateTime> {


    public void initialize(ValidBirthDate constraintAnnotation) {
        // Inizializzazione opzionale
    }

    public boolean isValid(LocalDateTime reservDate, ConstraintValidatorContext context) {
        if (reservDate == null) {
            return false; // La data non può essere nulla
        }

        LocalDateTime minDate = LocalDateTime.now();
        LocalDateTime maxDate = LocalDateTime.now().plusWeeks(6);

        return (reservDate.isAfter(minDate) || reservDate.isEqual(minDate)) &&
                (reservDate.isBefore(maxDate) || reservDate.isEqual(maxDate));
    }

}
