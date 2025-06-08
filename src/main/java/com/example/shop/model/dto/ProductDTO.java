package com.example.shop.model.dto;

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
public class ProductDTO {
    Long id ;
    private String name;
    private String describes;
    private Float price;
    private String images;
    private Long brand;
    private Users users;
    private Long categories;
}
