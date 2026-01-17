package org.mahedi.productservice.service;

import org.mahedi.core.dto.Product;
import org.mahedi.productservice.entity.ProductEntity;
import org.mahedi.productservice.error.ProductServiceException;
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
    public Product reserve(UUID orderId, UUID productId, Integer orderedQuantity) {
        ProductEntity productEntity = productRepository.findById(productId).orElseThrow();

        if (orderedQuantity > productEntity.getQuantity()) {
            throw new ProductServiceException.ProductInsufficientQuantityException(productId, orderId);
        }

        productEntity.setQuantity(productEntity.getQuantity() - orderedQuantity);
        productRepository.save(productEntity);

        Product responseProduct = new Product();
        BeanUtils.copyProperties(productEntity, responseProduct);
        return responseProduct;
    }

    @Override
    public void cancelReservation(UUID cancelledProductId, Integer quantity) {
        ProductEntity productEntity = productRepository.findById(cancelledProductId).orElseThrow();
        productEntity.setQuantity(productEntity.getQuantity() + quantity);
        productRepository.save(productEntity);
    }
}
