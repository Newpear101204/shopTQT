package com.example.shop.service.impl;


import com.example.shop.entity.*;
import com.example.shop.model.request.OrderRequest;
import com.example.shop.model.response.OrderResponse;
import com.example.shop.repository.*;
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
    private MemoriesRepository memoriesRepository;

    @Autowired
    private CartItemRepository cartItemRepository;


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
            orders_item.setMemoriesId(cartItem.getMemoriesId());
            orders_item.setPrice(cartItem.getProduct().getPrice());
            orders_item.setQuantity(cartItem.getNumber());
            orders_item.setOrders(orders); // Thiết lập quan hệ ngược lại
            orders_items.add(orders_item);
            total += (cartItem.getProduct().getPrice()*cartItem.getNumber());
        }
        orders.setTotal(total+20000);
        orders.setOrders_items(orders_items);

        // xoa tat ca trong cartitem.
        List<Cart_Item> list = user.getCart_items();
        for (Cart_Item cartItem : list) {
            cartItemRepository.deleteById(cartItem.getId());
        }
        
        orderRepository.save(orders);
    }

    @Override
    public List<OrderResponse> listOrders() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = UsersRepository.findByUsername(username);
        List<Orders> orders = user.getOrders();
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Orders order : orders) {
            List<Orders_item> orders_items = order.getOrders_items();
            for (Orders_item orders_item : orders_items) {
                Product product = orders_item.getProduct();
                OrderResponse orderResponse = new OrderResponse();
                orderResponse.setId(orders_item.getId());
                orderResponse.setDate(order.getCreatedDate());
                orderResponse.setStatus(order.getStatus());
                orderResponse.setName(product.getName());
                orderResponse.setPrice(product.getPrice());
                orderResponse.setAddress(order.getShippingAdress());
                orderResponse.setCapicity(memoriesRepository.findById(orders_item.getId()).get().getCapacity());
                orderResponse.setImages(product.getProductImages().get(0).getUrl());
                orderResponses.add(orderResponse);
            }
        }

        return orderResponses;
    }
}
