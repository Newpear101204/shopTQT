package com.example.shop.model.response;


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
public class ProductResponse {
    private Long id ;
    private String name;
    private String describes;
    private Float price;
    private List<String> images;
    private String brands;
    private String categories;
    private List<String> memories;
}
