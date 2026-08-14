package com.nexusprocure.inventory.service.Impl;

import com.nexusprocure.exception.ResourceNotFoundException;
import com.nexusprocure.inventory.dto.response.InventoryMovementResponse;
import com.nexusprocure.inventory.entity.InventoryMovement;
import com.nexusprocure.inventory.mapper.InventoryMovementMapper;
import com.nexusprocure.inventory.repository.InventoryMovementRepository;
import com.nexusprocure.inventory.repository.InventoryRepository;
import com.nexusprocure.inventory.service.InventoryMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryMovementServiceImpl implements InventoryMovementService {
    private final InventoryMovementMapper inventoryMovementMapper;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final InventoryRepository inventoryRepository;
    @Override
    public Page<InventoryMovementResponse>  getMovementHistory(Long inventoryId, Pageable pageable){
        if(!inventoryRepository.existsById(inventoryId)){
            throw new ResourceNotFoundException("Inventory not found with id: " + inventoryId);
        }
        Page<InventoryMovement> movementPage = inventoryMovementRepository.findByInventory_Id(inventoryId, pageable);

        return movementPage.map(inventoryMovementMapper::toResponse);
    }
}

