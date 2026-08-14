package com.nexusprocure.common.kafka;

import com.nexusprocure.common.event.stockissue.StockIssueApprovedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
//Spring publishes the business event internally → a listener/component receives it → Kafka publisher sends that event to Kafka.
public class StockIssueEventPublisher {
    private final KafkaTemplate<String, StockIssueApprovedEvent> kafkaTemplate;
    public void publishStockIssueApproved(StockIssueApprovedEvent event){
        // Receives the internally created Stock Issue Approved event and publishes it to Kafka.
        kafkaTemplate.send(KafkaTopics.STOCK_ISSUE_EVENTS,
                // Use the Stock Issue ID as the Kafka message key.
        // Kafka uses the key when deciding which partition
        // should receive the message.)
                event.stockIssueId().toString(),
                event);

    }



}
