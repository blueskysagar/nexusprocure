package com.nexusprocure.inventory.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record StockIssueRequest(
        @NotNull(message = "warehouse is required")
        Long warehouseId,
        @NotNull(message = "RequestedBy is required")
        Long requestedById,
        String reason,
        @Valid
        @NotEmpty(message = "At least one item is required")
        List<StockIssueItemRequest> items

) {
}
