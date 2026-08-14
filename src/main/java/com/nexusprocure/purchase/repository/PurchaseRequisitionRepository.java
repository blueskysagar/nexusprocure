package com.nexusprocure.purchase.repository;

import com.nexusprocure.purchase.entity.PurchaseRequisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PurchaseRequisitionRepository extends JpaRepository<PurchaseRequisition, Long>, JpaSpecificationExecutor<PurchaseRequisition> {
   Optional<PurchaseRequisition> findByRequisitionNumber(String requisitionNumber);
   boolean existsByRequisitionNumber(String requisitionNumber);
   Optional<PurchaseRequisition> findTopByOrderByIdDesc();
}
