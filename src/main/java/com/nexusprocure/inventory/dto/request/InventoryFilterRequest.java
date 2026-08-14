package com.nexusprocure.inventory.dto.request;

import com.nexusprocure.inventory.enums.InventoryStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryFilterRequest {
    private Long productId;
    private Long warehouseId;
    private Integer minimumQuantity;
    private Integer maximumQuantity;
    private InventoryStatus inventoryStatus;
}
