package com.example.shop.service;

import com.example.shop.model.dto.LoginDTO;
import com.example.shop.model.dto.RegisterDTO;
import com.example.shop.model.request.ProductToCartRequest;
import com.example.shop.model.request.Requests;
import com.example.shop.model.response.LoginResponse;
import com.example.shop.model.response.OrderAdminResponse;

import java.util.List;

public interface UsersService {
    LoginResponse login (LoginDTO loginDTO);
    void register (RegisterDTO registerDTO);
    void deleteUser (Long id);
    void ChooseProduct (ProductToCartRequest request);
    List<Requests> listOrderUser();

    List<OrderAdminResponse> listOrderAdmin(List<Long> ids);

}
