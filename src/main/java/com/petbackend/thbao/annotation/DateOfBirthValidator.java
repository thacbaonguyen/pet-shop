package com.petbackend.thbao.annotation;

import com.petbackend.thbao.exceptions.InvalidDateException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class DateOfBirthValidator implements ConstraintValidator<ValidDateOfBirth, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        LocalDate eighteenYearAgo = LocalDate.now().minusYears(18);
        if (value == null || !value.isBefore(eighteenYearAgo)){
            return false;
        }
        return true;
    }
}
