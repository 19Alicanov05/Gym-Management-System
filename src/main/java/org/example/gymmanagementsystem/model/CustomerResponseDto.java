package org.example.gymmanagementsystem.model;

import lombok.Getter;
import lombok.Setter;
import org.example.gymmanagementsystem.enums.MembershipType;

import java.math.BigDecimal;

@Getter
@Setter
public class CustomerResponseDto {


    private String name;
    private String surname;
    private String cardNumber;
    private Integer trainerId;
    private MembershipType membershipType;
    private String email;
    private BigDecimal debt;
}
