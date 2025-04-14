package org.example.gymmanagementsystem.service;

import org.example.gymmanagementsystem.model.CustomerResponseDto;
import org.example.gymmanagementsystem.model.CustomersDto;
import org.example.gymmanagementsystem.model.CustomerRequestDto;
import org.example.gymmanagementsystem.model.GymEntryLogDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CustomerService {

    Page<CustomersDto> getFilteredCustomers(Pageable pageable,
                                            String name,
                                            String surname,
                                            LocalDate birthDate,
                                            Boolean isActive,
                                            String trainerId);

    void updateCustomer(Integer id, CustomersDto customersDto);



    CustomerResponseDto registerCustomer(CustomerRequestDto customerDTO);

    CustomersDto getCustomersById(Integer id);


    void deleteCustomer(Integer id);
    List<GymEntryLogDto> getCustomerEntries(Integer customerId);
    List<GymEntryLogDto> getAllEntries();
}
