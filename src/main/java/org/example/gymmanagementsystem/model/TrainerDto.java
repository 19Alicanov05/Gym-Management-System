package org.example.gymmanagementsystem.model;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.gymmanagementsystem.customValidation.annotation.BirthDate;
import java.time.LocalDate;

@Data
public class TrainerDto {

    private Integer id;

    @Pattern(regexp = "^[A-Za-z]+$", message = "Name should only contain alphabetic characters (A-Z or a-z) and must not be empty.")
    private String name;

    @Pattern(regexp = "^[A-Za-z]+$", message = "Surname should only contain alphabetic characters (A-Z or a-z) and must not be empty.")
    private String surname;

    private Double salary;

    @NotNull(message = "Birthday cannot be null.")
    @BirthDate(message = "Birthday must be a valid date in the past and cannot be in the future.")
    private LocalDate birthDate;

}
