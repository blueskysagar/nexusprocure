package com.nexusprocure.common.event.purchaseorder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PurchaseOrderApprovedApplicationEvent {
    private final PurchaseOrderApprovedEvent event;


}
