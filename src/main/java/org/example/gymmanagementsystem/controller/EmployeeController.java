package org.example.gymmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gymmanagementsystem.model.EmployeesDto;
import org.example.gymmanagementsystem.service.impl.EmployeeServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeServiceImpl staffEmployeesService;

    @GetMapping
    @Operation(
            summary = "Get all employees"
    )
    public List<EmployeesDto> getAllEmployees() {
        return staffEmployeesService.getAllEmployees();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete employee"
    )
    public void deleteEmployee(@PathVariable @Valid Integer id) {
        staffEmployeesService.deleteEmployee(id);
    }

    @PostMapping("/add")
    @Operation(
            summary = "Add employee"
    )
    public void addEmployee(@RequestBody @Valid EmployeesDto employeesDto) {
        staffEmployeesService.addEmployee(employeesDto);
    }
}
