package com.example.shop.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterDTO {
    String username;
    String password;
    String confirmPassword;
    String email;
    String phone;
    String role;
}
