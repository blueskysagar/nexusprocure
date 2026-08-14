package com.nexusprocure.common.event.stockissue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StockIssueApprovedApplicationEvent {
    private final StockIssueApprovedEvent stockIssueApprovedEvent;
    // Holds the business event that will be sent to kafka.

}
