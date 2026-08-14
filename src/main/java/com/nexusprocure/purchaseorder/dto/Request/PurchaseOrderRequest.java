package com.nexusprocure.purchaseorder.dto.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PurchaseOrderRequest {
    @NotNull(message = "Purchase Requisitionid is required")
    private Long purchaseRequisitionId;
    @NotNull(message = "VendorId is required")
    private Long vendorId;
    @NotNull(message = "OrderDate is required")
    private LocalDate orderDate;
    @NotNull(message = "TotalAmount is required")
    @Positive(message = "TotalAmount must be greater than zero")
    private BigDecimal totalAmount;
    @NotNull(message = "Warehouse is required.")
    private Long warehouseId;


}
