package com.nexusprocure.inventory.service;

import com.nexusprocure.common.event.stockissue.StockIssueApprovedEvent;

public interface InventoryStockIssueService {
    void processStockIssue(StockIssueApprovedEvent event);
    // Inventory need some service to perform product 1:20, 2: 5 and later 10 and 9
}
