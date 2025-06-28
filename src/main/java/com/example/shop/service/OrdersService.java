package com.example.shop.service;


import com.example.shop.model.request.OrderRequest;
import org.springframework.stereotype.Service;


public interface OrdersService {
    void addOrder(OrderRequest orderRequest);
}
