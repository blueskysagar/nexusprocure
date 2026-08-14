package com.nexusprocure.inventory.service;

import com.nexusprocure.inventory.dto.request.StockIssueRequest;
import com.nexusprocure.inventory.dto.response.StockIssueResponse;

public interface StockIssueService {
    StockIssueResponse createStockIssue(StockIssueRequest request);
    StockIssueResponse getStockIssueById(Long id);
    StockIssueResponse approveStockIssue(Long stockIssueId, Long approverId);

}
