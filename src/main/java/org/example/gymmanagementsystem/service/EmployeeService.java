package org.example.gymmanagementsystem.service;

import org.example.gymmanagementsystem.model.EmployeesDto;

import java.util.List;

public interface EmployeeService {
    List<EmployeesDto> getAllEmployees();
    void addEmployee(EmployeesDto employeesDto);
    void deleteEmployee(Integer id);
}
