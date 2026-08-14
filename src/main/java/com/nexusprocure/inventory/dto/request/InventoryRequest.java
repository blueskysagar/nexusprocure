package com.nexusprocure.inventory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class InventoryRequest {
    @NotNull(message = "Product Id is required")
    private Long productId;
    @NotNull(message = "Warehouse Id is required")
    private Long warehouseId;
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
    @NotNull(message = "Minimum stock is required")
    @Min(value = 0, message = "Minimum stock cannot be negative")
    private Integer minimumStock;
    @NotNull(message = "Maximum stock is required")
    @Min(value = 0, message = "Maximum stock cannot be negative")
    private Integer maximumStock;

}
