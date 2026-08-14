package com.nexusprocure.inventory.service;

import com.nexusprocure.common.event.purchaseorder.PurchaseOrderApprovedEvent;

public interface InventoryReceivingService {
    void receive(PurchaseOrderApprovedEvent event);
}
