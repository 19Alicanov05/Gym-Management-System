package org.example.gymmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.gymmanagementsystem.dao.entity.PurchaseLogEntity;

import org.example.gymmanagementsystem.service.PurchaseLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/purchase-logs")
@RequiredArgsConstructor
public class PurchaseLogController {


    private final PurchaseLogService purchaseLogService;

    @GetMapping("/customer/{customerId}")
    @Operation(
            summary = "Get logs by customer"
    )
    public ResponseEntity<List<PurchaseLogEntity>> getLogsByCustomer(@PathVariable Integer customerId) {
        return purchaseLogService.getLogsByCustomer(customerId);
    }


    @GetMapping
    @Operation(
            summary = "Get all logs"
    )
    public ResponseEntity<List<PurchaseLogEntity>> getAllLogs() {
        return purchaseLogService.getAllLogs();
    }
}
