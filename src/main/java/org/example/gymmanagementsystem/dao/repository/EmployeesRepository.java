package org.example.gymmanagementsystem.dao.repository;

import org.example.gymmanagementsystem.dao.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeesRepository extends JpaRepository<EmployeeEntity,Integer> {
}
