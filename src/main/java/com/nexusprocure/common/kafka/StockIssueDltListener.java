package com.nexusprocure.common.kafka;

import com.nexusprocure.common.event.stockissue.StockIssueApprovedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

// DLT is parking place for all failed messages we want something to observe those message
@Component
@Slf4j
public class StockIssueDltListener {
    @KafkaListener(
            topics = KafkaTopics.STOCK_ISSUE_EVENTS + "-dlt",
            groupId = "stock-issue-dlt-group"
    )
    public void handle(StockIssueApprovedEvent event){
        // This listener receives Stock Issue messages
        // that failed all normal processing and ended up in the DLT.
        log.error(
                "Stock Issue event moved to DLT. stockIssueId={}", event.stockIssueId()
        );
    }


}
