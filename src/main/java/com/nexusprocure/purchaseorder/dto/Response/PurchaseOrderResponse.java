package com.nexusprocure.purchaseorder.dto.Response;

import com.nexusprocure.purchaseorder.entity.PurchaseOrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PurchaseOrderResponse {
    private Long id;
    private String purchaseRequisitionNumber;
    private String vendorName;
    private PurchaseOrderStatus status;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String lastModifiedBy;

}
