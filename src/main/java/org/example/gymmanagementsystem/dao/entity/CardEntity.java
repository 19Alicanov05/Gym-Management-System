package org.example.gymmanagementsystem.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cards")
@Getter
@Setter
public class CardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String cardNumber;

    private Integer remainingEntries;

    @OneToOne(mappedBy = "card")
    private CustomerEntity customer;
}
