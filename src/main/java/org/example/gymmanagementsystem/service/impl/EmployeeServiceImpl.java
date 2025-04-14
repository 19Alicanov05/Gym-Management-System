package org.example.gymmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gymmanagementsystem.dao.repository.EmployeesRepository;
import org.example.gymmanagementsystem.exceptions.NotFoundException;
import org.example.gymmanagementsystem.mapper.EmployeesMapper;
import org.example.gymmanagementsystem.model.EmployeesDto;
import org.example.gymmanagementsystem.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeesRepository employeesRepository;
    private final EmployeesMapper employeesMapper;

    @Override
    public List<EmployeesDto> getAllEmployees() {
        log.info("Started getAllPersonalEmployees method ");
        var personals = employeesRepository.findAll();
        return employeesMapper.toDtoList(personals);
    }

    @Override
    public void deleteEmployee(Integer id) {
        boolean exists = employeesRepository.existsById(id);

        if (!exists) {
            log.error("Attempt to delete employee failed. ID {} not found.", id);
            throw new NotFoundException("Employee with ID " + id + " not found");
        }

        employeesRepository.deleteById(id);
        log.info("Employee with ID {} deleted successfully", id);
    }


    @Override
    public void addEmployee(EmployeesDto employeesDto) {
        log.info("Started addPersonalEmployee method ");
        var personalEmployee = employeesMapper.toEntity(employeesDto);
        employeesRepository.save(personalEmployee);
        log.info("End addPersonalEmployee method ");
    }
}
