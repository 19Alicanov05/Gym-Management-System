package org.example.gymmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gymmanagementsystem.dao.repository.CustomersRepository;
import org.example.gymmanagementsystem.exceptions.ValidationException;
import org.example.gymmanagementsystem.service.ProductPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductPaymentServiceImpl implements ProductPaymentService {

    private final CustomersRepository customersRepository;

    @Override
    public ResponseEntity<String> paymentProduct(Integer customerId, Double amount) {
        log.info("Starting payment process for customer ID: {}", customerId);

        var customerOpt = customersRepository.findById(customerId);

        if (customerOpt.isEmpty()) {
            log.error("Customer with ID {} not found", customerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found.");
        }

        var customer = customerOpt.get();
        BigDecimal debt = customer.getDebt();

        log.debug("Customer {} current debt: {}", customerId, debt);

        if (debt.compareTo(BigDecimal.ZERO) == 0) {
            log.warn("Customer {} has no debt, payment attempt blocked", customerId);
            throw new ValidationException("Customer has no debt");
        }

        BigDecimal paymentAmount = BigDecimal.valueOf(amount);
        if (paymentAmount.compareTo(debt) > 0) {
            log.warn("Customer {} attempted to overpay. Blocked.", customerId);
            throw new ValidationException("Payment amount cannot exceed the debt.");
        }

        BigDecimal newDebt = debt.subtract(paymentAmount).max(BigDecimal.ZERO);
        customer.setDebt(newDebt);
        customersRepository.save(customer);

        log.info("Payment completed for customer {}. New debt: {}", customerId, newDebt);

        return ResponseEntity.ok("Payment successful. Remaining debt: " + newDebt);
    }

}
