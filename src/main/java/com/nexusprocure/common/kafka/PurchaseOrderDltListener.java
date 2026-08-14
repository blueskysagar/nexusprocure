package com.nexusprocure.common.kafka;

import com.nexusprocure.common.event.purchaseorder.PurchaseOrderApprovedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
@Component
@Slf4j
public class PurchaseOrderDltListener {

    @KafkaListener(
                topics = KafkaTopics.PURCHASE_ORDER_EVENTS + "-dlt",
                groupId = "purchase-order-dlt-group",
                containerFactory = "kafkaListenerContainerFactory"
        )
        public void handle(PurchaseOrderApprovedEvent event) {
            // This listener receives Purchase Order messages
            // that failed all normal processing and ended up in the DLT.
           log.error(
                    "Purchase Order event moved to DLT. event={}", event
            );
        }
    }

