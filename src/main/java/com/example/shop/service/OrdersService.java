package com.example.shop.service;


import com.example.shop.model.request.OrderRequest;
import com.example.shop.model.response.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.List;


public interface OrdersService {
    void addOrder(OrderRequest orderRequest);
    List<OrderResponse> listOrders();
}
