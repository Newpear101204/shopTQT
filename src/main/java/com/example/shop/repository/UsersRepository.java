package com.example.shop.repository;

import com.example.shop.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
    boolean existsByPhoneOrEmail(String phone , String email);
}
