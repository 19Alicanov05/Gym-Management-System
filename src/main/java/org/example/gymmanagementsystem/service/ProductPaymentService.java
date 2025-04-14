package org.example.gymmanagementsystem.service;

import org.springframework.http.ResponseEntity;

public interface ProductPaymentService {
    ResponseEntity<String> paymentProduct(Integer customerId, Double amount);
}
