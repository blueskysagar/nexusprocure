package com.nexusprocure.inventory.specification;

import com.nexusprocure.inventory.entity.Inventory;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class InventoryFetchSpecification {
    private InventoryFetchSpecification(){}
    public static Specification<Inventory> fetchProductAndWarehouse(){
        return (root, query, criteriaBuilder) -> {
            if(query !=null && query.getResultType() != Long.class){
                root.fetch("product", JoinType.INNER);
                root.fetch("warehouse", JoinType.INNER);
                query.distinct(true);
            }
            return criteriaBuilder.conjunction();
        };
    }
}
