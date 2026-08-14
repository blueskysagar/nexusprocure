package com.nexusprocure.inventory.specification;

import com.nexusprocure.inventory.entity.Inventory;
import com.nexusprocure.inventory.enums.InventoryStatus;
import com.nexusprocure.product.entity.Product;
import com.nexusprocure.warehouse.entity.Warehouse;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class InventorySpecification {
    private InventorySpecification(){}
    public static Specification<Inventory> hasProduct(Long productId){
        return (root, query, criteriaBuilder) -> {
            if (productId == null){
                return null;
            }
            Join<Inventory, Product> productJoin = root.join("product");
            return criteriaBuilder.equal(productJoin.get("id"),productId);

        };
    }
    public static Specification<Inventory> hasWarehouse(Long warehouseId){
        return (root, query, criteriaBuilder) -> {
            if(warehouseId == null){
                return null;
            }
            Join<Inventory, Warehouse> warehouseJoin = root.join("warehouse");
            return criteriaBuilder.equal(warehouseJoin.get("id"), warehouseId);
        };
    }
    public static Specification<Inventory> hasQuantityRange(Integer minimumQuantity, Integer maximumQuantity){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(minimumQuantity !=null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("quantity"), minimumQuantity));
            }
            if(maximumQuantity !=null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("quantity"), maximumQuantity));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };


    }
    public static Specification<Inventory> hasInventoryStatus(InventoryStatus inventorystatus){
        return (root, query, criteriaBuilder) -> {
            if(inventorystatus == null){
                return null;
            }
            return criteriaBuilder.equal(root.get("inventoryStatus"),inventorystatus );
        };
    }


}
