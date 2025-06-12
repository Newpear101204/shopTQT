package com.example.shop.service.impl;

import com.example.shop.entity.Product;
import com.example.shop.entity.ProductImage;
import com.example.shop.model.dto.ProductDTO;
import com.example.shop.model.response.ProductResponse;
import com.example.shop.repository.BrandRepository;
import com.example.shop.repository.CategoriesRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            ProductResponse productResponse = new ProductResponse();
            List<String> imageUrls = product.getImages().stream()
                    .map(ProductImage::getUrl)
                    .collect(Collectors.toList());
            productResponse.setImages(imageUrls);
            productResponse.setId(product.getId());
            productResponse.setName(product.getName());
            productResponse.setDescribes(product.getDescribes());
            productResponse.setPrice(product.getPrice());
            productResponse.setBrand(brandRepository.findById(product.getBrand().getId()).get().getName());
            productResponse.setCategories(categoriesRepository.findById(product.getCategories().getId()).get().getName());
            productResponses.add(productResponse);
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

        List<ProductImage> imageEntities = productDTO.getImages().stream()
                .map(url -> ProductImage.builder()
                        .url(url)
                        .product(product)
                        .build())
                .collect(Collectors.toList());

        product.setImages(imageEntities);

        productRepository.save(product);
    }
}
