package com.nexusprocure.purchaseorder.dto.Request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PurchaseOrderUpdateRequest {
    @NotNull(message = "Vendor Id is required")
    private Long vendorId;
    @NotNull(message = "Order Date is required")
    private LocalDate orderDate;
    @NotNull(message = "total Amount is required")
    @DecimalMin(value = "0.01", message = "Total Amount must be greater than zero.")
    private BigDecimal totalAmount;
}
