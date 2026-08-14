package com.nexusprocure.inventory.dto.response;

import com.nexusprocure.inventory.enums.InventoryMovementType;


import java.time.LocalDateTime;

public record InventoryMovementResponse(
        Long id,
        InventoryMovementType movementType,
         Integer quantity,
         String referenceNumber,
         LocalDateTime movementDate
) {

}
