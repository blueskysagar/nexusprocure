package com.nexusprocure.common.kafka;
import org.springframework.kafka.core.KafkaTemplate;

import com.nexusprocure.common.event.purchaseorder.PurchaseOrderApprovedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PurchaseOrderEventPublisher {
    private final KafkaTemplate<String, PurchaseOrderApprovedEvent> kafkaTemplate;
    public void publishPurchaseOrderApproved(PurchaseOrderApprovedEvent event){
        kafkaTemplate.send(
                KafkaTopics.PURCHASE_ORDER_EVENTS,
                event.purchaseOrderId().toString(),
                event
        );
    }

}
