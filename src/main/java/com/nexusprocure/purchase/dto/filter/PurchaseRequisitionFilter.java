package com.nexusprocure.purchase.dto.filter;

import com.nexusprocure.purchase.entity.RequisitionPriority;
import com.nexusprocure.purchase.entity.RequisitionStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PurchaseRequisitionFilter {
    private RequisitionStatus status;
    private String department;
    private Long requesterId;
    private RequisitionPriority priority;
    private LocalDate requiredDateFrom;
    private LocalDate requiredDateTo;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private String keyword;


}
