package org.example.gymmanagementsystem.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers_req_log")
@Getter
@Setter
public class CustomerReqLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime regDate;

    @Column(nullable = false)
    private String methodName;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customerEntity;

    @Column(nullable = false)
    private Boolean deleted = false;
}
