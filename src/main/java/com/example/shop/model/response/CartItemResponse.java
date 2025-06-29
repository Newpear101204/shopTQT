package com.example.shop.model.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    Long id ; // di cua cartitem
    String name ;
    String images ;
    int number ;
    String capacity;
    Float price;
    Long productId;
}
