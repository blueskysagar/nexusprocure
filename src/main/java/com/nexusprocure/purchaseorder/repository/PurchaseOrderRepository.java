package com.nexusprocure.purchaseorder.repository;

import com.nexusprocure.purchase.entity.PurchaseRequisition;
import com.nexusprocure.purchaseorder.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long>, JpaSpecificationExecutor<PurchaseOrder> {
    Optional<PurchaseOrder> findByPurchaseOrderNumber(String purchaseOrderNumber);
    boolean existsByPurchaseOrderNumber(String purchaseOrderNumber);
    Optional<PurchaseOrder> findTopByOrderByIdDesc();
    boolean existsByPurchaseRequisition(PurchaseRequisition purchaseRequisition);
}
