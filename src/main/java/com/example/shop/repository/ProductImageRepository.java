package com.example.shop.repository;

import com.example.shop.entity.Product;
import com.example.shop.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProducttId(Long producttId);
//    void deleteByProductt(Product product);
}
