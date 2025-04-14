package org.example.gymmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.gymmanagementsystem.service.ProductPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductPaymentController {
    private final ProductPaymentService productPaymentService;

    @PostMapping("/payment")
    @Operation(
            summary = "Payment product"
    )
    public ResponseEntity<String> paymentProduct(@RequestParam Integer customerId,
                                              @RequestParam Double amount) {
        return productPaymentService.paymentProduct(customerId, amount);
    }

}
