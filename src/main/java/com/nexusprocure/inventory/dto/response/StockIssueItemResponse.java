package com.nexusprocure.inventory.dto.response;

public record StockIssueItemResponse(
        Long id,
        Long productId,
        String productName,
        Integer quantity
) {
}
