package com.nexusprocure.purchaseorder.dto.Filter;

import com.nexusprocure.purchaseorder.entity.PurchaseOrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PurchaseOrderFilterRequest {
    private PurchaseOrderStatus status;
    private Long vendorId;
    private String keyword;
    private LocalDate fromDate;
    private LocalDate toDate;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private String requisitionNumber;

}
