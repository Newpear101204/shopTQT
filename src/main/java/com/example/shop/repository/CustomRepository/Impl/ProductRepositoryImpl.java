package com.example.shop.repository.CustomRepository.Impl;

import com.example.shop.entity.Product;
import com.example.shop.entity.Users;
import com.example.shop.model.request.ProductRequest;
import com.example.shop.repository.CustomRepository.ProductRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    public static String CheckSQL (ProductRequest productRequest) {
        StringBuilder sql = new StringBuilder("");
                if (productRequest.getBrandId() != null) {
                    sql.append(" and b.brands_id = " + productRequest.getBrandId() );

                }
                if (productRequest.getCategoryId() != null) {
                    sql.append(" and b.categories_id = " + productRequest.getCategoryId() );
                }
                if (productRequest.getName() != null ||
                        productRequest.getName().equalsIgnoreCase("")) {
                    sql.append(" and b.name like '%" + productRequest.getName() + "%'");

                }
        return sql.toString();
    }

    @Override
    public List<Product> searchProducts(ProductRequest productRequest) {
        StringBuilder sql = new StringBuilder("SELECT b.* FROM PRODUCT b ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(CheckSQL(productRequest));
        Query quey = entityManager.createNativeQuery(sql.toString(), Product.class);
        List<Product> arr = quey.getResultList();
        return quey.getResultList();
    }
}
