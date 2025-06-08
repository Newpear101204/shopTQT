package com.example.shop.model.response;


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
public class ProductResponse {
    private String name;
    private String describes;
    private Float price;
    private String images;
    private Brand brand;
    private Users users;
    private Categories categories;
}
