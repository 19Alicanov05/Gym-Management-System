package org.example.gymmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gymmanagementsystem.dao.entity.CustomerEntity;
import org.example.gymmanagementsystem.dao.entity.PurchaseLogEntity;
import org.example.gymmanagementsystem.dao.repository.CustomersRepository;
import org.example.gymmanagementsystem.dao.repository.PurchaseLogRepository;
import org.example.gymmanagementsystem.exceptions.NotFoundException;
import org.example.gymmanagementsystem.service.PurchaseLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PurchaseLogServiceImpl implements PurchaseLogService {
    private final PurchaseLogRepository purchaseLogRepository;
    private final CustomersRepository customersRepository;

    @Override
    public ResponseEntity<List<PurchaseLogEntity>> getLogsByCustomer(Integer customerId) {
        log.info("Fetching purchase logs for customer with ID: {}", customerId);

        CustomerEntity customer = customersRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.error("Customer with ID {} not found.", customerId);
                    return new NotFoundException("Customer not found");
                });

        log.info("Customer found: {} {}", customer.getName(), customer.getSurname());

        List<PurchaseLogEntity> logs = purchaseLogRepository.findByCustomerNameAndCustomerSurname(
                customer.getName(), customer.getSurname()
        );

        log.info("Found {} purchase logs for customer {}", logs.size(), customer.getName() + " " + customer.getSurname());

        return ResponseEntity.ok(logs);
    }

    @Override
    public ResponseEntity<List<PurchaseLogEntity>> getAllLogs() {
        log.info("Fetching all purchase logs.");

        List<PurchaseLogEntity> logs = purchaseLogRepository.findAll();

        log.info("Found {} total purchase logs.", logs.size());

        return ResponseEntity.ok(logs);
    }
}
