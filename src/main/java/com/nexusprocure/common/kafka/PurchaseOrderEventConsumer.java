package com.nexusprocure.common.kafka;

import com.nexusprocure.common.event.purchaseorder.PurchaseOrderApprovedEvent;
import com.nexusprocure.inventory.service.InventoryReceivingService;
import com.nexusprocure.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PurchaseOrderEventConsumer {
    private final InventoryReceivingService inventoryReceivingService;


    /**
     * Listens for Purchase Order Approved events published to Kafka.
     *
     * Spring creates the Kafka consumer, continuously polls the broker,
     * deserializes JSON into PurchaseOrderApprovedEvent,
     * and invokes this method whenever a new message arrives.
     */
    @KafkaListener(
            topics = KafkaTopics.PURCHASE_ORDER_EVENTS,
            groupId = "purchase-order-group"
    )
    public void consume(PurchaseOrderApprovedEvent event){
        log.info(
                "Purchase Order Approved Event received. purchaseOrderId={}, purchaseOrderNumber={}, vendorId={}, totalAmount={}", event.purchaseOrderId(),
                event.purchaseOrderNumber(),
                event.vendorId(),
                event.totalAmount()
        );
        inventoryReceivingService.receive(event);

    }


}
