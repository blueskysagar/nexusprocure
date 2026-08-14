package com.nexusprocure.inventory.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryResponse {
    private Long productId;
    private String productCode;
    private Long warehouseId;
    private String warehouseCode;
    private String warehouseName;
    private Integer quantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;
    private Integer minimumStock;
    private Integer maximumStock;
}
