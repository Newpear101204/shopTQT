package com.example.shop.repository.CustomRepository;

import com.example.shop.entity.Product;
import com.example.shop.model.request.ProductRequest;

import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> searchProducts (ProductRequest productRequest);
}
