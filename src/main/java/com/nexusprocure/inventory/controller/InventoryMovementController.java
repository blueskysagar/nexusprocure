package com.nexusprocure.inventory.controller;

import com.nexusprocure.inventory.dto.response.InventoryMovementResponse;
import com.nexusprocure.inventory.service.InventoryMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/inventories")
@RequiredArgsConstructor
public class InventoryMovementController {
    private final InventoryMovementService inventoryMovementService;
    @GetMapping("/{inventoryId}/movements")
    public Page<InventoryMovementResponse> getMovementHistory(@PathVariable Long inventoryId, @PageableDefault(
            size = 20,
            sort = "movementDate",
            direction = Sort.Direction.DESC
    ) Pageable pageable){
        return inventoryMovementService.getMovementHistory(inventoryId, pageable);
    }
}
