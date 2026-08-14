package com.nexusprocure.common.event.purchaseorder;

import java.math.BigDecimal;

public record PurchaseOrderItemEvent(
    Long productId,
    Long warehouseId,
    Integer quantity,
    BigDecimal unitPrice
    )
{

}

