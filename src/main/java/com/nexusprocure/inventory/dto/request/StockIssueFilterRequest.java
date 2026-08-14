package com.nexusprocure.inventory.dto.request;

import com.nexusprocure.inventory.enums.StockIssueStatus;

import java.time.LocalDate;

public record StockIssueFilterRequest(
        String issueNumber,
        StockIssueStatus status,
        Long warehouseId,
        Long requestedById,
        LocalDate fromDate,
        LocalDate toDate
) {
}
