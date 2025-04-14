package org.example.gymmanagementsystem.customValidation.validator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.gymmanagementsystem.customValidation.annotation.BirthDate;
import java.time.LocalDate;

public class BirthDateValidator implements ConstraintValidator<BirthDate, LocalDate> {

    private static final int MIN_AGE = 16;
    private static final int MAX_YEARS = 100;

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        LocalDate today = LocalDate.now();
        LocalDate earliestValidDate = today.minusYears(MAX_YEARS);
        LocalDate minimumAgeDate = today.minusYears(MIN_AGE);

        if (value.isAfter(today)) {
            return false;
        }

        if (value.isEqual(today)) {
            return false;
        }

        if (value.isBefore(earliestValidDate)) {
            return false;
        }

        return !value.isAfter(minimumAgeDate);
    }
}
