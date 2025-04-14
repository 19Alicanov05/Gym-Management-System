package org.example.gymmanagementsystem.specification;

import org.example.gymmanagementsystem.dao.entity.CustomerEntity;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomerSpecification {

    public static Specification<CustomerEntity> byNameContains(String name) {
        return (root, _, criteriaBuilder) -> {
            String pattern = "%" + name.toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern);
        };
    }

    public static Specification<CustomerEntity> bySurnameContains(String surname) {
        return (root, _, criteriaBuilder) -> {
            String pattern = "%" + surname.toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), pattern);
        };
    }

    public static Specification<CustomerEntity> byBirthDate(LocalDate birthDate) {
        return (root, _, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("birthDate"), birthDate);
    }

    public static Specification<CustomerEntity> byTrainer(Integer trainerId) {
        return (root, _, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("trainer").get("id"), trainerId);
    }

    public static Specification<CustomerEntity> byIsActive(boolean isActive) {
        return (root, _, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isActive"), isActive);
    }





}
