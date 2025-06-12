package com.example.shop.repository;

import com.example.shop.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository  extends JpaRepository<Brand, Long> {
}
