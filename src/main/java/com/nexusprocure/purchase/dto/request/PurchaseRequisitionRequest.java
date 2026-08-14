package com.nexusprocure.purchase.dto.request;

import com.nexusprocure.purchase.entity.RequisitionPriority;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseRequisitionRequest {
    @NotBlank(message = "title is required")
    private String title;
    @NotBlank(message = "description is required")
    private String description;
    @NotBlank(message = "department is required")
    private String department;
    @NotNull(message = "priority is required")
    private RequisitionPriority priority;
    @NotNull(message ="Total amount is required")
    @Positive(message = "Total Amount must be greater than zero")
    private BigDecimal totalAmount;
    @NotNull(message = "Required date is required")
    @Future(message = "Required date must be in future")
    private LocalDate requiredAt;


}
