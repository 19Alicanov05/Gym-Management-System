package org.example.gymmanagementsystem.service;

import org.example.gymmanagementsystem.dao.entity.PurchaseLogEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PurchaseLogService {
    ResponseEntity<List<PurchaseLogEntity>> getLogsByCustomer( Integer customerId);
    ResponseEntity<List<PurchaseLogEntity>> getAllLogs();
}
