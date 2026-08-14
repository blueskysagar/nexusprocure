package com.nexusprocure.inventory.repository;


import com.nexusprocure.inventory.entity.InventoryMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {
Page<InventoryMovement> findByInventory_Id(Long inventoryId, Pageable pageable);
}
