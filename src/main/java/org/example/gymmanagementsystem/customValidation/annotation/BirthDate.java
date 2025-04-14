package org.example.gymmanagementsystem.customValidation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.gymmanagementsystem.customValidation.validator.BirthDateValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BirthDateValidator.class)
public @interface BirthDate {
    String message() default "Invalid birth date. Must not be in the future or exceed the next century.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
