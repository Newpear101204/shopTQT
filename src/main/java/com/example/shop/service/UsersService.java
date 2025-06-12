package com.example.shop.service;

import com.example.shop.model.dto.LoginDTO;
import com.example.shop.model.dto.RegisterDTO;
import com.example.shop.model.response.LoginResponse;

public interface UsersService {
    LoginResponse login (LoginDTO loginDTO);
    void register (RegisterDTO registerDTO);
    void deleteUser (Long id);

}
