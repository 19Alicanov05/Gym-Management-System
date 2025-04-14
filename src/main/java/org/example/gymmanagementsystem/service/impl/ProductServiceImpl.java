package org.example.gymmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gymmanagementsystem.dao.entity.CustomerEntity;
import org.example.gymmanagementsystem.dao.entity.ProductEntity;
import org.example.gymmanagementsystem.dao.entity.PurchaseLogEntity;
import org.example.gymmanagementsystem.dao.repository.CustomersRepository;
import org.example.gymmanagementsystem.dao.repository.ProductRepository;
import org.example.gymmanagementsystem.dao.repository.PurchaseLogRepository;
import org.example.gymmanagementsystem.enums.MembershipType;
import org.example.gymmanagementsystem.exceptions.NotFoundException;
import org.example.gymmanagementsystem.exceptions.ValidationException;
import org.example.gymmanagementsystem.mapper.ProductMapper;
import org.example.gymmanagementsystem.model.ProductDto;
import org.example.gymmanagementsystem.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CustomersRepository customerRepository;
    private final PurchaseLogRepository purchaseLogRepository;

    public void purchaseProduct(Integer customerId, Integer productId) {
        log.info("Starting purchase of product {} for customer {}", productId, customerId);
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));


        BigDecimal currentDebt = customer.getDebt() == null
                ? BigDecimal.ZERO
                : customer.getDebt();

        if (customer.getIsActive().equals(true) && customer.getMembershipType().equals(MembershipType.PREMIUM)) {
            customer.setDebt(currentDebt.add(BigDecimal.valueOf(product.getPrice())));


            customerRepository.save(customer);

            PurchaseLogEntity log = new PurchaseLogEntity();
            log.setCustomerName(customer.getName());
            log.setCustomerSurname(customer.getSurname());
            log.setProductName(product.getName());
            log.setProductPrice(product.getPrice());
            log.setQuantityUnit(product.getQuantityUnit());
            log.setPurchaseDate(LocalDateTime.now());

            purchaseLogRepository.save(log);


        } else {
            throw new ValidationException("This customer is not eligible to purchase");
        }
        log.info("Finished purchase of product {} for customer {}", productId, customerId);
    }


    @Override
    public List<ProductDto> getAllProducts() {
        log.info("Starting all products");
        var products = productRepository.findAll();
        log.info("Finished all products");
        return productMapper.toDtoList(products);

    }

    @Override
    public void addProduct(ProductDto productDto) {
        log.info("Starting adding product {}", productDto);
        var product = productMapper.toEntity(productDto);
        log.info("Finished adding product {}", product);
        productRepository.save(product);

    }
}
