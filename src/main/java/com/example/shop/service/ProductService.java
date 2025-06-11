package com.example.shop.service;

import com.example.shop.model.dto.ProductDTO;
import com.example.shop.model.response.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProductService {
    List<ProductResponse> getAllProducts();
    void deleteProduct(Long id);
    void createOrUpdateProduct(ProductDTO productDTO);

}
