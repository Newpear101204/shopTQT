package com.example.shop.service;

import com.example.shop.model.response.CartItemResponse;

import java.util.List;

public interface CartItemService {
    void deleteCartItem(Long cartItemId);
    List<CartItemResponse> getCartItems();
}
