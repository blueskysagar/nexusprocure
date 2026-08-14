package com.nexusprocure.inventory.specification;

import com.nexusprocure.inventory.dto.request.InventoryFilterRequest;
import com.nexusprocure.inventory.entity.Inventory;
import org.springframework.data.jpa.domain.Specification;

public class InventorySpecificationBuilder {
    private InventorySpecificationBuilder(){}
    public static Specification<Inventory> build(InventoryFilterRequest request)
    {
        Specification<Inventory> specification = InventoryFetchSpecification.fetchProductAndWarehouse();
        if(request.getProductId() !=null){
            specification = specification.and(InventorySpecification.hasProduct(request.getProductId()));
        }
        if(request.getWarehouseId() !=null){
            specification = specification.and(InventorySpecification.hasWarehouse(request.getWarehouseId()));

        }
        if(request.getMinimumQuantity() !=null || request.getMaximumQuantity() !=null){
            specification = specification.and(InventorySpecification.hasQuantityRange(request.getMinimumQuantity(), request.getMaximumQuantity()));
        }
        if(request.getInventoryStatus() !=null){
            specification = specification.and(InventorySpecification.hasInventoryStatus(request.getInventoryStatus()));
        }
        return specification;
    }

}
