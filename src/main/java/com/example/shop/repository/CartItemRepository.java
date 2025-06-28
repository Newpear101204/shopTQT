package com.example.shop.repository;

import com.example.shop.entity.Cart_Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<Cart_Item, Long> {
}
