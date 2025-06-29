package com.example.shop.service.impl;

import com.example.shop.entity.Cart_Item;
import com.example.shop.entity.Users;
import com.example.shop.model.response.CartItemResponse;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.MemoriesRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UsersRepository;
import com.example.shop.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private MemoriesRepository memoriesRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public List<CartItemResponse> getCartItems() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = usersRepository.findByUsername(username);
        List<Cart_Item> cartItemList = user.getCart_items();
        List<CartItemResponse> cartItemResponseList = new ArrayList<>();
        for (Cart_Item cartItem : cartItemList) {
            CartItemResponse cartItemResponse = new CartItemResponse();
            cartItemResponse.setId(cartItem.getId());
            cartItemResponse.setName(cartItem.getProduct().getName());
            cartItemResponse.setPrice(cartItem.getProduct().getPrice());
//            cartItemResponse.setCapacity(memoriesRepository.findById(cartItem.getMemoriesId()).get().getCapacity());
            cartItemResponse.setCapacity(cartItem.getMemoriesId().toString());
            cartItemResponse.setNumber(cartItem.getNumber());
            cartItemResponse.setImages(cartItem.getProduct().getProductImages().get(0).getUrl());
            cartItemResponse.setProductId(cartItem.getProduct().getId());
            cartItemResponseList.add(cartItemResponse);

        }
        return cartItemResponseList;
    }
}
