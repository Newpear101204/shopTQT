package com.example.shop.service.impl;

import com.example.shop.entity.Product;
import com.example.shop.model.dto.ProductDTO;
import com.example.shop.model.response.ProductResponse;
import com.example.shop.repository.ProductRepository;
import com.example.shop.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    ModelMapper modelMapper;


    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            productResponses.add(modelMapper.map(product, ProductResponse.class));
        }
        return productResponses;
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void createOrUpdateProduct(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        productRepository.save(product);
    }
}
