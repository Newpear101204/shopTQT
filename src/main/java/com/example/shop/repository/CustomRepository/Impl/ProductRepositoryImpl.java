package com.example.shop.repository.CustomRepository.Impl;

import com.example.shop.entity.Product;
import com.example.shop.model.request.ProductRequest;
import com.example.shop.repository.CustomRepository.ProductRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> searchProducts(ProductRequest productRequest) {
        return List.of();
    }
}
