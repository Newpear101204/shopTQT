package com.example.shop.repository;

import com.example.shop.entity.Product;
import com.example.shop.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
