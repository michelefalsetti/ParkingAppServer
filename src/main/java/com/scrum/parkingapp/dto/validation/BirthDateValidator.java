package com.scrum.parkingapp.dto.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class BirthDateValidator implements ConstraintValidator<ValidBirthDate, LocalDate> {

    @Override
    public void initialize(ValidBirthDate constraintAnnotation) {
        // Inizializzazione opzionale
    }

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) {
            return false; // La data di nascita non deve essere null
        }

        LocalDate minDate = LocalDate.of(1910, 1, 1);
        LocalDate maxDate = LocalDate.now().minusYears(13);

        return (birthDate.isAfter(minDate) || birthDate.isEqual(minDate)) &&
                (birthDate.isBefore(maxDate) || birthDate.isEqual(maxDate));
    }
}
