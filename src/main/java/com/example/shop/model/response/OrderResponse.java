package com.example.shop.model.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Long id;
    String name;
    String address;
    String number;
    Float price;
    Date date;
    String status;
    String capicity;
    String images;
}
