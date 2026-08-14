package com.nexusprocure.purchase.dto.response;

import com.nexusprocure.purchase.entity.RequisitionStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseRequisitionResponse {
    private Long id;
    private String requisitionNumber;
    private String title;
    private String description;
    private String department;
    private RequisitionStatus status;
    private BigDecimal totalAmount;
    private LocalDate requiredDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long requestedById;
    private String requestedByName;

}
