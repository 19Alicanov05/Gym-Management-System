package org.example.gymmanagementsystem.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.example.gymmanagementsystem.customValidation.annotation.BirthDate;

import java.time.LocalDate;

@Data
public class EmployeesDto {
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name can only contain alphabetic characters (A-Z or a-z) without spaces or special symbols.")
    private String name;
    @Pattern(regexp = "^[A-Za-z]+$", message = "Surname can only contain alphabetic characters (A-Z or a-z) without spaces or special symbols.")
    private String surname;
    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Salary format is invalid")
    private Double salary;

    @BirthDate
    private LocalDate birthDate;
}
