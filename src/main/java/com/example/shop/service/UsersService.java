package com.example.shop.service;

import com.example.shop.model.dto.LoginDTO;
import com.example.shop.model.dto.RegisterDTO;

public interface UsersService {
    String login (LoginDTO loginDTO);
    void register (RegisterDTO registerDTO);
    void deleteUser (Long id);

}
