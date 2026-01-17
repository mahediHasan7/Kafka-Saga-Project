package org.mahedi.productservice.service;

import org.mahedi.core.dto.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> findAll();

    Product save(Product product);

    Product reserve(UUID orderId, UUID productId, Integer orderedQuantity);

    void cancelReservation(UUID cancelledProductId, Integer quantity);
}
