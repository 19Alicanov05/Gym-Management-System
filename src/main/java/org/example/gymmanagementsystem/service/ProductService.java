package org.example.gymmanagementsystem.service;

import org.example.gymmanagementsystem.model.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();
    void addProduct(ProductDto productDto);
    void purchaseProduct(Integer customerId, Integer productId);
}
