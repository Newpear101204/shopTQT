package com.example.shop.model.request;


import com.example.shop.entity.Brand;
import com.example.shop.entity.Categories;
import com.example.shop.entity.Users;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    Long categoryId;
    String name;
    Long brandId;
    Integer sort; // 1 la tang dan , 2 la giam dan.
}
