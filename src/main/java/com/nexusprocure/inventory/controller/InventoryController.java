package com.nexusprocure.inventory.controller;
import com.nexusprocure.common.Response.PageResponse;
import com.nexusprocure.inventory.dto.request.InventoryFilterRequest;
import com.nexusprocure.inventory.dto.request.InventoryRequest;
import com.nexusprocure.inventory.dto.response.InventoryResponse;
import com.nexusprocure.inventory.dto.update.InventoryUpdateRequest;
import com.nexusprocure.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventories")
public class InventoryController {
    private final InventoryService inventoryService;
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(@Valid @RequestBody InventoryRequest request){
        InventoryResponse response = inventoryService.createInventory(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> getInventoryById(@PathVariable Long id){
        InventoryResponse response = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(response);

    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<PageResponse<InventoryResponse>> getAllInventories(@ParameterObject InventoryFilterRequest request, @ParameterObject Pageable pageable){
        return ResponseEntity.ok(inventoryService.getAllInventories(request, pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<InventoryResponse> updateInventory(@PathVariable Long id, @Valid @RequestBody InventoryUpdateRequest request){
        InventoryResponse response =
                inventoryService.updateInventory(id, request);

        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activateInventory(@PathVariable Long id){
        inventoryService.activateInventory(id);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateInventory(@PathVariable Long id){
        inventoryService.deactivateInventory(id);
        return ResponseEntity.noContent().build();
    }



}
