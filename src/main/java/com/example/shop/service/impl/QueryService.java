package com.example.shop.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import jakarta.persistence.metamodel.Attribute;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class QueryService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Map<String, Object>> runSQLQuery(String sql) {
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> rows = query.getResultList();
        List<String> columnNames = query.getResultList().isEmpty() ? List.of() :
                entityManager.getMetamodel().getEntities().stream()
                        .filter(e -> e.getJavaType().getSimpleName().equalsIgnoreCase("product")) // tùy chỉnh
                        .flatMap(e -> e.getAttributes().stream())
                        .map(Attribute::getName)
                        .toList();

        List<Map<String, Object>> result = new ArrayList<>();
        for (Object row : rows) {
            Object[] columns = row instanceof Object[] ? (Object[]) row : new Object[]{row};
            Map<String, Object> rowMap = new LinkedHashMap<>();
            for (int i = 0; i < columns.length; i++) {
                rowMap.put("col" + (i + 1), columns[i]); // không cần tên cột nếu chưa biết chắc
            }
            result.add(rowMap);
        }
        return result;
    }
}
