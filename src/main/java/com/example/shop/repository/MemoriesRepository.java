package com.example.shop.repository;

import com.example.shop.entity.Memories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoriesRepository extends JpaRepository<Memories, Long> {
}
