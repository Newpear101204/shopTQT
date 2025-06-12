package com.example.shop.service.impl;

import com.example.shop.entity.Product;
import com.example.shop.entity.ProductImage;
import com.example.shop.model.dto.ProductDTO;
import com.example.shop.model.request.ProductRequest;
import com.example.shop.model.response.ProductResponse;
import com.example.shop.repository.BrandRepository;
import com.example.shop.repository.CategoriesRepository;
import com.example.shop.repository.MemoriesRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private MemoriesRepository memoriesRepository;

    @Autowired
    ModelMapper modelMapper;


    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            ProductResponse productResponse = new ProductResponse();
            List<String> imageUrls = product.getProductImages().stream()
                    .map(ProductImage::getUrl)
                    .collect(Collectors.toList());
            productResponse.setImages(imageUrls);
            productResponse.setName(product.getName());
            productResponse.setDescribes(product.getDescribes());
            productResponse.setPrice(product.getPrice());
            productResponse.setBrands(brandRepository.findById(product.getBrand().getId()).get().getName());
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
      //  Product product = modelMapper.map(productDTO, Product.class);
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescribes(productDTO.getDescribes());
        product.setPrice(productDTO.getPrice());
        List<ProductImage> imageEntities = productDTO.getImages().stream()
                .map(url -> ProductImage.builder()
                        .url(url)
                        .product(product)
                        .build())
                .collect(Collectors.toList());

        product.setProductImages(imageEntities);
        product.setBrand(brandRepository.findById(productDTO.getBrand()).get());
        product.setCategories(categoriesRepository.findById(productDTO.getCategories()).get());
        product.setMemories(memoriesRepository.findById(productDTO.getMemories()).get());
        productRepository.save(product);

    }

    @Override
    public List<ProductResponse> searchProduct(ProductRequest productRequest) {
        List<Product> products = productRepository.searchProducts(productRequest);
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            productResponses.add(modelMapper.map(product, ProductResponse.class));
        }
        if (productRequest.getSort() == 1){
            productResponses.sort(Comparator.comparingDouble(ProductResponse::getPrice));
        }else if (productRequest.getSort() == 2){
            productResponses.sort(Comparator.comparingDouble(ProductResponse::getPrice).reversed());
        }
        return productResponses;
    }
}
