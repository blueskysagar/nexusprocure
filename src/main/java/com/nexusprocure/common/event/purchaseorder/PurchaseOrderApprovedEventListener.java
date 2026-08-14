package com.nexusprocure.common.event.purchaseorder;

import com.nexusprocure.common.kafka.PurchaseOrderEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PurchaseOrderApprovedEventListener {
    private final PurchaseOrderEventPublisher purchaseOrderEventPublisher;
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(
            PurchaseOrderApprovedApplicationEvent applicationEvent
    ){
        purchaseOrderEventPublisher.publishPurchaseOrderApproved(applicationEvent.getEvent());
    }
}
