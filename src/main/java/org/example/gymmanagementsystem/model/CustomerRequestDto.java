package org.example.gymmanagementsystem.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.example.gymmanagementsystem.customValidation.annotation.BirthDate;
import org.example.gymmanagementsystem.enums.MembershipType;

import java.time.LocalDate;

@Data
public class CustomerRequestDto {
    @NotBlank(message = "Name cannot be empty")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name can only contain alphabetic characters (A-Z or a-z) without spaces or special symbols.")
    private String name;
    @NotBlank(message = "Surname cannot be empty")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Surname can only contain alphabetic characters (A-Z or a-z) without spaces or special symbols.")
    private String surname;
    @BirthDate
    private LocalDate birthDate;
    @Positive
    @Digits(integer = 10, fraction = 0, message = "Trainer ID must be a valid number")
    private Integer trainerId;

    @NotNull(message = "Membership type must be selected")
    private MembershipType membershipType=MembershipType.STANDARD;
    @Email(message = "Invalid email format")
    private String email;

}
