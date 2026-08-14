package com.nexusprocure.inventory.service;

import com.nexusprocure.inventory.dto.response.InventoryMovementResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryMovementService {
    Page<InventoryMovementResponse> getMovementHistory(Long inventoryId, Pageable pageable);
}
