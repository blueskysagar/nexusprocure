package com.nexusprocure.inventory.mapper;

import com.nexusprocure.inventory.dto.request.InventoryRequest;
import com.nexusprocure.inventory.dto.response.InventoryResponse;
import com.nexusprocure.inventory.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "inventoryStatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "reservedQuantity", ignore = true)
    @Mapping(target = "version", ignore = true)
    Inventory toEntity(InventoryRequest request);
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productCode", source = "product.productCode")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "warehouseCode", source = "warehouse.warehouseCode")
    @Mapping(target = "warehouseName", source = "warehouse.name")
   //can perform Derived mappings in this case Available quantity
        @Mapping(target = "availableQuantity", expression = "java(inventory.getQuantity() - inventory.getReservedQuantity())")
    InventoryResponse toResponse(Inventory inventory);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "inventoryStatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromRequest(InventoryRequest request, @MappingTarget Inventory inventory);
}
