package com.example.shop.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderAdminResponse {
    Long id ; // id cua orders
    String username; // ten khach
    Date date;
    String status;
    String productName; // ten san pham
    String image;
    int number;
    String adress;
    Float price;
    String capicity;
}
