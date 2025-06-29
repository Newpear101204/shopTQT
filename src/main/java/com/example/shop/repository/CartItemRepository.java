package com.example.shop.repository;

import com.example.shop.entity.Cart_Item;
import com.example.shop.entity.Product;
import com.example.shop.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<Cart_Item, Long> {
    Optional<Cart_Item> findByUsersAndProductAndMemoriesId(Users users, Product product, Long memoriesId);

}
