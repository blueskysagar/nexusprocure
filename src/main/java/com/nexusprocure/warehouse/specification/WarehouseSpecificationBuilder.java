package com.nexusprocure.warehouse.specification;

import com.nexusprocure.warehouse.dto.Filter.WarehouseFilterRequest;
import com.nexusprocure.warehouse.entity.Warehouse;
import org.springframework.data.jpa.domain.Specification;

public class WarehouseSpecificationBuilder {
    public static Specification<Warehouse> build(WarehouseFilterRequest request) {
        Specification<Warehouse> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        if (request.getStatus() != null) {
            specification = specification.and(WarehouseSpecification.hasStatus(request.getStatus()));
        }
        if (request.getManagerId() != null) {
            specification = specification.and(
                    WarehouseSpecification.hasManager(request.getManagerId())
            );
        }
        if(request.getKeyword() !=null && !request.getKeyword().isBlank()){
            specification = specification.and(WarehouseSpecification.hasKeyword(request.getKeyword()));
        }
        if(request.getMinCapacity() !=null || request.getMaxCapacity() !=null){
            specification = specification.and(WarehouseSpecification.hasCapacityRange(request.getMinCapacity(), request.getMaxCapacity()));
        }
       return specification;
    }
}
