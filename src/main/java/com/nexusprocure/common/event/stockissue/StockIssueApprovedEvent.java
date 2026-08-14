package com.nexusprocure.common.event.stockissue;

import java.time.LocalDateTime;
import java.util.List;

public record StockIssueApprovedEvent(
        Long stockIssueId,
        String issueNumber,
        Long warehouseId,
        Long approvedBy,
        LocalDateTime approvedAt,
        List<StockIssueItemEvent> items // Parents which is StockIssueApprovedEvent
             // tells us what business action happened and child tell us which individual
        //items were affected by that actions.
) {
}
