package org.mahedi.productservice.controller;

import jakarta.validation.Valid;
import org.mahedi.core.dto.Product;
import org.mahedi.productservice.dto.ProductCreationRequest;
import org.mahedi.productservice.dto.ProductCreationResponse;
import org.mahedi.productservice.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        List<Product> products = productService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping
    ResponseEntity<ProductCreationResponse> saveProduct(@RequestBody @Valid ProductCreationRequest request) {
        Product product = new Product();
        BeanUtils.copyProperties(request, product);
        Product savedProd = productService.save(product);
        ProductCreationResponse response = new ProductCreationResponse();
        BeanUtils.copyProperties(savedProd, response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
