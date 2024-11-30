package com.scrum.parkingapp.exception;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ExpirationYearValidator implements ConstraintValidator<ValidExpirationYear, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/yy");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        System.out.println(value);

        try {
            YearMonth expirationDate = YearMonth.parse(value, FORMATTER);
            System.out.println(expirationDate);
            boolean res =  expirationDate.isAfter(YearMonth.now());
            System.out.println(res);
            return res;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
