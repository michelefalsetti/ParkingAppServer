package com.scrum.parkingapp.dto.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ReservationDateValidator implements ConstraintValidator<ValidReservDate, LocalDate> {


    public void initialize(ValidBirthDate constraintAnnotation) {
        // Inizializzazione opzionale
    }

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) {
            return false; // La data di nascita non deve essere null
        }

        LocalDate minDate = LocalDate.now();
        LocalDate maxDate = LocalDate.now().plusWeeks(6);

        return (birthDate.isAfter(minDate) || birthDate.isEqual(minDate)) &&
                (birthDate.isBefore(maxDate) || birthDate.isEqual(maxDate));
    }
}
