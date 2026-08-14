package com.nexusprocure.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "processed_purchase_orders",
uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_processed_purchase_order",
                columnNames = "purchase_order_id"
        )
})
@Getter
@Setter
@NoArgsConstructor

public class ProcessedPurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "purchase_order_id", nullable = false)
    private Long purchaseOrderId;
    @Column(nullable = false)
    private LocalDateTime processedAt;

}
