package org.example.gymmanagementsystem.service.impl;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.gymmanagementsystem.dao.entity.CardEntity;
import org.example.gymmanagementsystem.dao.entity.CustomerEntity;
import org.example.gymmanagementsystem.dao.entity.GymEntryLogEntity;
import org.example.gymmanagementsystem.dao.entity.TrainerEntity;
import org.example.gymmanagementsystem.dao.repository.CustomersRepository;
import org.example.gymmanagementsystem.dao.repository.GymEntryLogRepository;
import org.example.gymmanagementsystem.dao.repository.TrainerRepository;
import org.example.gymmanagementsystem.enums.MembershipType;
import org.example.gymmanagementsystem.exceptions.NotFoundException;
import org.example.gymmanagementsystem.exceptions.ValidationException;
import org.example.gymmanagementsystem.mapper.CustomersMapper;
import org.example.gymmanagementsystem.mapper.GymEntryLogMapper;
import org.example.gymmanagementsystem.model.CustomerResponseDto;
import org.example.gymmanagementsystem.model.CustomersDto;
import org.example.gymmanagementsystem.model.CustomerRequestDto;
import org.example.gymmanagementsystem.model.GymEntryLogDto;
import org.example.gymmanagementsystem.service.CustomerService;
import org.example.gymmanagementsystem.specification.CustomerSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomersRepository customersRepository;
    private final CustomersMapper customersMapper;
    private final TrainerRepository trainerRepository;
    private final EmailServiceImpl emailService;
    private final GymEntryLogRepository gymEntryLogRepository;
    private final GymEntryLogMapper gymEntryLogMapper;

    @Override
    @Transactional
    public CustomerResponseDto registerCustomer(CustomerRequestDto customerDTO) {
        CustomerEntity customer = new CustomerEntity();
        customer.setName(customerDTO.getName());
        customer.setSurname(customerDTO.getSurname());
        customer.setBirthDate(customerDTO.getBirthDate());

        if (customerDTO.getTrainerId() != null) {

            Optional<TrainerEntity> trainer = trainerRepository.findById(customerDTO.getTrainerId());
            trainer.ifPresent(customer::setTrainer);
            trainer.ifPresent(t -> t.getCustomers().add(customer));

        }

        MembershipType selectedMembership = customerDTO.getMembershipType() != null
                ? customerDTO.getMembershipType()
                : MembershipType.STANDARD;
        customer.setMembershipType(selectedMembership);

        int allowedEntries = selectedMembership.getMonthlyEntries();
        customer.setRemainingEntries(allowedEntries);

        LocalDateTime today = LocalDateTime.now();
        customer.setSubscriptionStartDate(today);
        customer.setSubscriptionEndDate(today.plusMonths(1));

        if (customerDTO.getEmail() != null && !customerDTO.getEmail().isBlank()) {
            customer.setEmail(customerDTO.getEmail());
        }

        customer.setIsActive(true);

        String cardNumber = generateCardNumber();
        CardEntity card = new CardEntity();
        card.setCardNumber(cardNumber);
        card.setRemainingEntries(allowedEntries);
        customer.setCard(card);

        CustomerEntity savedCustomer = customersRepository.save(customer);

        if (customer.getEmail() != null && !customer.getEmail().isBlank()) {
            emailService.sendEmail(
                    customer.getEmail(),
                    "Welcome to Alicanov Fitness!",
                    "Hello " + customer.getName() + ",\n\n" +
                            "Welcome to Alicanov Fitness! Your membership has started. Enjoy your workout!\n\n" +
                            "Best regards,\n" +
                            "Alicanov Fitness Team"
            );
            log.info("📩 [{}] Welcome email sent to: {}", today, customer.getEmail());
        }

        return customersMapper.toResponseDto(savedCustomer);
    }




    private String generateCardNumber() {
        SecureRandom random = new SecureRandom();
        int number = 10000000 + random.nextInt(90000000);
        return String.valueOf(number);
    }





    @Override
    public CustomersDto getCustomersById(Integer id) {
        log.info("Start getCustomersById with id: {}", id);
        var customers = customersRepository.findById(id).
                orElseThrow(() -> new NotFoundException("CUSTOMER_NOT_FOUND ID: " + id));
        CustomersDto customersDto = customersMapper.toDto(customers);
        log.info("Finished getCustomersById with id: {}", id);
        return customersDto;
    }

    @Override
    public void updateCustomer(Integer id, CustomersDto customersDto) {
        CustomerEntity existingCustomer = customersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("CUSTOMER_NOT_FOUND"));

        log.info("Start updating customer with id: {}", id);

        if (customersDto.getName() != null) {
            existingCustomer.setName(customersDto.getName());
        }

        if (customersDto.getSurname() != null) {
            existingCustomer.setSurname(customersDto.getSurname());
        }

        if (null != customersDto.getBirthDate()) {
            existingCustomer.setBirthDate(customersDto.getBirthDate());
        }

        customersRepository.save(existingCustomer);

        log.info("Successfully updated customer with id: {}", id);
    }


    @Override
    public void deleteCustomer(Integer id) {
        boolean exists = customersRepository.existsById(id);

        if (!exists) {
            log.error("Attempt to delete customer failed. ID {} not found.", id);
            throw new NotFoundException("Customer with ID " + id + " not found");
        }

        customersRepository.deleteById(id);
        log.info("Customer with ID {} deleted successfully", id);
    }



    @Override
    public Page<CustomersDto> getFilteredCustomers(Pageable pageable,
                                                   String name,
                                                   String surname,
                                                   LocalDate birthDate,
                                                   Boolean isActive,
                                                   String trainerId) {

        log.info("Started getFilteredCustomers method");

        if (name != null && !name.matches("^[A-Za-z]+$")) {
            throw new ValidationException("Invalid format: Name must contain only letters");
        }

        if (surname != null && !surname.matches("^[A-Za-z]+$")) {
            throw new ValidationException("Invalid format: Surname must contain only letters");
        }

        Integer trainerIdInt = null;
        if (trainerId != null) {
            try {
                trainerIdInt = Integer.parseInt(trainerId);
                if (trainerIdInt <= 0) {
                    throw new ValidationException("Trainer ID must be a positive number");
                }
            } catch (NumberFormatException e) {
                throw new ValidationException("Invalid format: Trainer ID must be a number");
            }
        }

        Specification<CustomerEntity> specification = Specification.where(null);

        if (name != null && !name.isBlank()) {
            specification = specification.and(CustomerSpecification.byNameContains(name));
        }

        if (surname != null && !surname.isBlank()) {
            specification = specification.and(CustomerSpecification.bySurnameContains(surname));
        }

        if (birthDate != null) {
            specification = specification.and(CustomerSpecification.byBirthDate(birthDate));
        }

        if (trainerIdInt != null) {
            specification = specification.and(CustomerSpecification.byTrainer(trainerIdInt));
        }

        if (isActive != null) {
            specification = specification.and(CustomerSpecification.byIsActive(isActive));
        }


        Page<CustomerEntity> customersEntities = customersRepository.findAll(specification, pageable);

        if (customersEntities.isEmpty()) {
            log.warn("Customer not found with given filters");
            throw new NotFoundException("Customer not found");
        }

        List<CustomersDto> customersDtoList = customersEntities.stream()
                .map(customersMapper::toDto)
                .collect(Collectors.toList());

        log.info("Finished getFilteredCustomers method");

        return new PageImpl<>(customersDtoList, pageable, customersEntities.getTotalElements());
    }
    @Override
    public List<GymEntryLogDto> getCustomerEntries(Integer customerId) {
        log.info("Fetching entries for customer with ID: {}", customerId);

        List<GymEntryLogEntity> entries = gymEntryLogRepository.findByCustomerId(customerId);

        if (entries.isEmpty()) {
            log.warn("No entries found for customer ID: {}", customerId);
            throw new NotFoundException("No entries found for customer with ID " + customerId);
        }

        return gymEntryLogMapper.toDtoList(entries);
    }

    @Override
    public List<GymEntryLogDto>getAllEntries() {
        log.info("Fetching entries for customers");
        List<GymEntryLogEntity> entries = gymEntryLogRepository.findAll();
        log.info("Finished fetching entries for customers");
        return gymEntryLogMapper.toDtoList(entries);
    }


}




