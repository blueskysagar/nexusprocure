package com.nexusprocure.inventory.dto.update;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryUpdateRequest {
    @NotNull(message = "Minimum stock is required")
    @Min(value = 0, message = "Minimum stock cannot be negative")
    private Integer minimumStock;
    @NotNull(message = "Maximum stock is required")
    @Min(value = 0, message ="Maximum stock cannot be negative" )
    private Integer maximumStock;
}
