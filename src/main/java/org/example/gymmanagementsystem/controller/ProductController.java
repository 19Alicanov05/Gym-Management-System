package org.example.gymmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gymmanagementsystem.model.ProductDto;
import org.example.gymmanagementsystem.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping()
    @Operation(
            summary = "Get all products"
    )
    public List<ProductDto> getAllProducts() {
       return productService.getAllProducts();
    }

    @PostMapping("/add")
    @Operation(
            summary = "Add product "
    )
    public void addProduct(@RequestBody @Valid ProductDto productDto) {
        productService.addProduct(productDto);
    }

    @PostMapping("/purchase")
    @Operation(
            summary = "Purchase product"
    )
    public ResponseEntity<String> purchaseProduct(
            @RequestParam Integer customerId,
            @RequestParam Integer productId) {

        productService.purchaseProduct(customerId, productId);
        return ResponseEntity.ok("Product purchased and debt updated!");
    }

}
