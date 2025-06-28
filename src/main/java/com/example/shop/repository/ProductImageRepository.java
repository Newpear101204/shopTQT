package com.example.shop.repository;

import com.example.shop.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    Optional<ProductImage> findByUrlAndProducttId(String url, Long productId);

}
