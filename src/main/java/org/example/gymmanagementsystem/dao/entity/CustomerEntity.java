package org.example.gymmanagementsystem.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import org.example.gymmanagementsystem.enums.MembershipType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipType membershipType;
    @Column(unique = true)
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id")
    private CardEntity card;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private TrainerEntity trainer;

    @Column(nullable = false)
    private LocalDateTime subscriptionStartDate;
    @Column(nullable = false)
    private LocalDateTime subscriptionEndDate;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;
    private Integer remainingEntries;

    @OneToMany(mappedBy = "customerEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CustomerReqLogEntity> reqLogs = new HashSet<>();

    @Column(nullable = false)
    private BigDecimal debt = BigDecimal.ZERO;

}
