package com.example.shop.service;

import com.example.shop.model.dto.LoginDTO;
import com.example.shop.model.dto.RegisterDTO;

import java.util.Map;

public interface UsersService {
//    String login (LoginDTO loginDTO);
    Map<String, Object> login (LoginDTO loginDTO);
    void register (RegisterDTO registerDTO);
    void deleteUser (Long id);

}
