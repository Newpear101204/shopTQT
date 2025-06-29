package com.example.shop.service.impl;


import com.example.shop.entity.Cart_Item;
import com.example.shop.entity.Orders;
import com.example.shop.entity.Orders_item;
import com.example.shop.entity.Users;
import com.example.shop.model.request.OrderRequest;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UsersRepository;
import com.example.shop.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UsersRepository UsersRepository;

    @Autowired
    private ProductRepository ProductRepository;


    @Override
    public void addOrder(OrderRequest orderRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = UsersRepository.findByUsername(username);
        List<Cart_Item> cartItems = user.getCart_items();

        List<Orders_item> orders_items = new ArrayList<>();
        Orders orders = new Orders();
        orders.setUsers(user);
        orders.setStatus("OK");
        orders.setShipFee(20000);
        orders.setShippingAdress(orderRequest.getAdress());
        orders.setPaymentMethod(orderRequest.getPaymentMethod());

        int total = 0;
        for (Cart_Item cartItem : cartItems) {
            Orders_item orders_item = new Orders_item();
            orders_item.setProduct(cartItem.getProduct());
            orders_item.setPrice(cartItem.getProduct().getPrice());
            orders_item.setQuantity(cartItem.getNumber());
            orders_item.setOrders(orders); // Thiết lập quan hệ ngược lại
            orders_items.add(orders_item);
            total += (cartItem.getProduct().getPrice()*cartItem.getNumber());
        }
        orders.setTotal(total+20000);
        orders.setOrders_items(orders_items);

        orderRepository.save(orders);
    }
}
