package com.nexusprocure.common.event.stockissue;

import com.nexusprocure.common.kafka.StockIssueEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

// This new one listens to the Spring ApplicationEvent.
@Component
@RequiredArgsConstructor
public class StockIssueApprovedApplicationEventListener {
    private final StockIssueEventPublisher stockIssueEventPublisher;
    // Listen for the internal Spring event only after the Stock Issue
    // database transaction has successfully committed.
    //
    // This prevents us from publishing a Kafka event for a Stock Issue
    // whose database transaction ultimately rolled back.
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(StockIssueApprovedApplicationEvent applicationEvent){
        stockIssueEventPublisher.publishStockIssueApproved(applicationEvent.getStockIssueApprovedEvent());
     //// Receives the internal Spring event, extracts the Stock Issue Approved event, and sends it to Kafka.
    }
}
