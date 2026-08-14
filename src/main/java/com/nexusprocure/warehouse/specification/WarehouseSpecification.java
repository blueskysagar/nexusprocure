package com.nexusprocure.warehouse.specification;

import com.nexusprocure.user.entity.User;
import com.nexusprocure.warehouse.entity.Warehouse;
import com.nexusprocure.warehouse.entity.WarehouseStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class WarehouseSpecification {
    public static Specification<Warehouse> hasStatus(WarehouseStatus status){
        return (root, query, criteriaBuilder) -> {
            if(status == null){
                return null;
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }
    public static Specification<Warehouse> hasManager(Long managerId){
        return (root, query, criteriaBuilder) -> {
            Join<Warehouse, User> managerJoin = root.join("manager");
            return criteriaBuilder.equal(managerJoin.get("id"), managerId);
        };
    }
    public static Specification<Warehouse> hasKeyword(String keyword){
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("warehouseCode")), "%" + keyword.toLowerCase()  + "%"), criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"));
        };
    }
    public static Specification<Warehouse> hasCapacityRange(Integer minCapacity, Integer maxCapacity){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(minCapacity !=null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("capacity"), minCapacity));
            }
            if(maxCapacity !=null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("capacity"), maxCapacity));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
