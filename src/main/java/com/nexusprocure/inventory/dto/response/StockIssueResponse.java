package com.nexusprocure.inventory.dto.response;

import com.nexusprocure.inventory.enums.StockIssueStatus;

import java.time.LocalDateTime;
import java.util.List;

public record StockIssueResponse(
        Long id,
        String issueNumber,
        Long warehouseId,
        String warehouseName,
        Long requestedById,
        String requestedByName,
        Long approvedById,
        String approvedByName,
        StockIssueStatus status,
        String reason,
        LocalDateTime issuedDate,
        List<StockIssueItemResponse> items

) {
}
