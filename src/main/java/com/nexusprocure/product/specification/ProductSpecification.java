package com.nexusprocure.product.specification;

import com.nexusprocure.product.entity.Category;
import com.nexusprocure.product.entity.Product;
import com.nexusprocure.product.enums.ProductStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> hasProductStatus(ProductStatus productStatus){
        return (root, query, criteriaBuilder) -> {
            if(productStatus == null){
                return null;
            }
            return criteriaBuilder.equal(root.get("productStatus"), productStatus);
        };
    }
    public static Specification<Product> hasCategory(Long categoryId){
        return (root, query, criteriaBuilder) -> {
            Join<Product, Category> categoryJoin = root.join("category");
            return criteriaBuilder.equal(categoryJoin.get("id"), categoryId);
        };

    }
    public static Specification<Product> hasKeyword(String keyword){
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("productCode")), "%" + keyword.toLowerCase() + "%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" +keyword.toLowerCase() + "%")
        );

        }
        public static Specification<Product> hasPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (minPrice != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
                }
                if (maxPrice != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };

        }
    }

