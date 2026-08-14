package com.nexusprocure.inventory.repository;

import com.nexusprocure.inventory.entity.ProcessedPurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedPurchaseOrderRepository extends JpaRepository<ProcessedPurchaseOrder, Long> {
    boolean existsByPurchaseOrderId(Long purchaseOrderId);
}
