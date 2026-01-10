package org.mahedi.productservice.service;

import org.mahedi.core.dto.Product;
import org.mahedi.productservice.entity.ProductEntity;
import org.mahedi.productservice.exception.ProductInsufficientQuantityException;
import org.mahedi.productservice.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll().stream().map(p -> {
            Product product = new Product();
            BeanUtils.copyProperties(p, product);
            return product;
        }).collect(Collectors.toList());
    }

    @Override
    public Product save(Product product) {
        ProductEntity productEntity = new ProductEntity(product.getName(), product.getPrice(), product.getQuantity());
        productRepository.save(productEntity);
        return new Product(productEntity.getId(), productEntity.getName(), productEntity.getPrice(), productEntity.getQuantity());
    }

    @Override
    public Product reserve(Product product, UUID orderId) {
        ProductEntity productEntity = productRepository.findById(product.getId()).orElseThrow();

        if (product.getQuantity() > productEntity.getQuantity()) {
            throw new ProductInsufficientQuantityException(product.getId(), orderId);
        }

        productEntity.setQuantity(productEntity.getQuantity() - product.getQuantity());
        productRepository.save(productEntity);

        Product responseProduct = new Product();
        BeanUtils.copyProperties(productEntity, responseProduct);
        return responseProduct;
    }

    @Override
    public void cancelReservation(Product productToCancel) {
        ProductEntity productEntity = productRepository.findById(productToCancel.getId()).orElseThrow();
        productEntity.setQuantity(productEntity.getQuantity() + productToCancel.getQuantity());
        productRepository.save(productEntity);
    }
}
