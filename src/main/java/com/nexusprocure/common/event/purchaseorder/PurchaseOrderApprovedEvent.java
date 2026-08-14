package com.nexusprocure.common.event.purchaseorder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PurchaseOrderApprovedEvent(Long purchaseOrderId,
                                         String purchaseOrderNumber,
                                         Long vendorId,
                                         BigDecimal totalAmount,
                                         LocalDateTime approvedAt,
                                         List<PurchaseOrderItemEvent> items
                                         ){

}
