package com.nexusprocure.common.event.stockissue;

public record StockIssueItemEvent(
        Long  productId, // Product whose inventory needs to be reduced.
        //Number of quantity that need to be issued.
        Integer quantity
) {
}
