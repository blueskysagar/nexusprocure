package com.nexusprocure.inventory.mapper;

import com.nexusprocure.inventory.dto.response.InventoryMovementResponse;
import com.nexusprocure.inventory.entity.InventoryMovement;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryMovementMapper {
    InventoryMovementResponse toResponse(InventoryMovement movement);
    List<InventoryMovementResponse> toResponseList(List<InventoryMovement> movements);
}
