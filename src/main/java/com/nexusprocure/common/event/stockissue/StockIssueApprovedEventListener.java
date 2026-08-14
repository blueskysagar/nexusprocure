package com.nexusprocure.common.event.stockissue;

import com.nexusprocure.common.kafka.KafkaTopics;
import com.nexusprocure.inventory.service.InventoryStockIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockIssueApprovedEventListener {
    private final InventoryStockIssueService inventoryStockIssueService;
   // Hey Kafka whenever a message arrive on stock issue events, deliver it to this method
    @KafkaListener(
            topics = KafkaTopics.STOCK_ISSUE_EVENTS,
            groupId = "inventory-group", // consumer group
    containerFactory = "stockIssueKafkaListenerContainerFactory" // // Tell Spring which ListenerContainerFactory to use
            // for this Kafka listener.
    )
    public void handle(StockIssueApprovedEvent event){
        // Delegate or handle this responsibility to InventoryService
        inventoryStockIssueService.processStockIssue(event);
    }
}
