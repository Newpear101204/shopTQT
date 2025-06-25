package com.example.shop.model.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductToCartRequest {
    Long productId;
    int memoriesId;
    int number;
}
