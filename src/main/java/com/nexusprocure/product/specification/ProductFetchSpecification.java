package com.nexusprocure.product.specification;

import com.nexusprocure.product.entity.Product;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class ProductFetchSpecification {
    private ProductFetchSpecification(){}
    public static Specification<Product> fetchCategory(){
        return (root, query, criteriaBuilder) -> {
            if(query !=null && query.getResultType() != Long.class){
                root.fetch("category", JoinType.INNER);
                query.distinct(true);
            }
            return criteriaBuilder.conjunction();
        };

    }
}
