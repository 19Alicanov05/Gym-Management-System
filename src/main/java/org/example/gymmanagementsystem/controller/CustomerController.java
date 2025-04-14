package org.example.gymmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.gymmanagementsystem.model.CustomerResponseDto;
import org.example.gymmanagementsystem.model.CustomersDto;
import org.example.gymmanagementsystem.model.CustomerRequestDto;
import org.example.gymmanagementsystem.model.GymEntryLogDto;
import org.example.gymmanagementsystem.service.impl.CustomerServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
    private final CustomerServiceImpl customersService;

    @GetMapping("/filtered")
    @Operation(
            summary = "Get filtered customers",
            operationId = "getFilteredCustomers"
    )
    public ResponseEntity<Page<CustomersDto>> getFilteredCustomers(
            Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) LocalDate birthDate,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String trainerId) {

        log.info("Fetching filtered customers...");

        Page<CustomersDto> customers = customersService.getFilteredCustomers(
                pageable, name, surname, birthDate, isActive, trainerId
        );

        return ResponseEntity.ok(customers);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete Customer",
            operationId = "deleteCustomer"
    )
    public void deleteCustomer(@PathVariable Integer id) {
        log.info("Deleting Customer with id {}", id);
        customersService.deleteCustomer(id);
    }

    @PostMapping("/register")
    @Operation(
            summary = "Register customer",
            operationId = "registerCustomer"
    )
    public ResponseEntity<CustomerResponseDto> registerCustomer(@RequestBody @Valid CustomerRequestDto customerDTO) {
        CustomerResponseDto response = customersService.registerCustomer(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/{id}")
    @Operation(

            summary = "Get customer By Id",
            operationId = "getCustomersById"

    )
    public CustomersDto getCustomersById(@PathVariable @Valid Integer id) {
        return customersService.getCustomersById(id);
    }

    @PutMapping("/{id}")
    @Operation(

            summary = "Update Customer",
            operationId = "updateCustomer"

    )
    public void updateCustomer(@PathVariable Integer id, @RequestBody @Valid CustomersDto customers) {
        customersService.updateCustomer(id, customers);
    }


    @GetMapping("/customer/{customerId}/entries")
    @Operation(
            summary = "Get all entries of a customer",
            operationId = "getCustomerEntries"
    )
    public List<GymEntryLogDto> getCustomerEntries(@PathVariable Integer customerId) {

        return customersService.getCustomerEntries(customerId);
    }

    @GetMapping("/entries")
    @Operation(
            summary = "Get all entries"
    )
    public List<GymEntryLogDto> getAllEntries() {
        return customersService.getAllEntries();
    }

}



