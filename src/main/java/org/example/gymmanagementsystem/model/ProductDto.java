package org.example.gymmanagementsystem.model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductDto {
    @Pattern(regexp = "^[A-Za-z]+$", message = "Product name can only contain alphabetic characters (A-Z or a-z) without spaces or special symbols.")
    private String name;
    @Positive
    private Double price;
}
