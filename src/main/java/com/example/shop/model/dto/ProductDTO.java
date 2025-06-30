package com.example.shop.model.dto;

import com.example.shop.entity.Brand;
import com.example.shop.entity.Categories;
import com.example.shop.entity.Users;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {
    private Long id ;
    private String name;
    private String describes;
    private Float price;
    private List<String> images;
    private Long brand;
    private Long categories;
    private List<Long> memories;
    private int bestseller;
}
