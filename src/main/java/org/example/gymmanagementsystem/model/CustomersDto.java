package org.example.gymmanagementsystem.model;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.gymmanagementsystem.customValidation.annotation.BirthDate;
import org.example.gymmanagementsystem.enums.MembershipType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CustomersDto {
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name can only contain alphabetic characters (A-Z or a-z) without spaces or special symbols.")
    private String name;
    @Pattern(regexp = "^[A-Za-z]+$", message = "Surname can only contain alphabetic characters (A-Z or a-z) without spaces or special symbols.")
    private String surname;
    @BirthDate
    private LocalDate birthDate;
    private Integer trainerId;
    private String cardNumber;
    private MembershipType membershipType;
    private Boolean isActive;
    @Digits(integer = 10, fraction = 0, message = "Debt must be a valid number")
    private BigDecimal debt;



}
