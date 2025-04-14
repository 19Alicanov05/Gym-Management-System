package org.example.gymmanagementsystem.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String customerSurname;

    private String productName;
    private Double productPrice;
    private String quantityUnit;

    private LocalDateTime purchaseDate;
}
