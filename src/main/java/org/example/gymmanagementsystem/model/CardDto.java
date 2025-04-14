package org.example.gymmanagementsystem.model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CardDto {


    @Size(min = 8, max = 8, message = "Card number must be exactly 8 characters long.")
    @Pattern(regexp = "^[0-9]{8}$", message = "Card number must contain exactly 8 digits.")
    private String cardNumber;
}