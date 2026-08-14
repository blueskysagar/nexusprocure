package com.nexusprocure.warehouse.controller;

import com.nexusprocure.warehouse.dto.Filter.WarehouseFilterRequest;
import com.nexusprocure.warehouse.dto.Request.WarehouseRequest;
import com.nexusprocure.warehouse.dto.Response.WarehouseResponse;
import com.nexusprocure.warehouse.dto.Request.WarehouseUpdateRequest;
import com.nexusprocure.warehouse.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/warehouses")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PostMapping
    public ResponseEntity<WarehouseResponse> createWarehouse(@Valid @RequestBody WarehouseRequest request){
        WarehouseResponse response = warehouseService.createWarehouse(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponse> getWarehouseById(@PathVariable Long id){
        WarehouseResponse response = warehouseService.getWarehouseById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<Page<WarehouseResponse>> getAllWarehouses(Pageable pageable, @ModelAttribute WarehouseFilterRequest request) {
        Page<WarehouseResponse> response = warehouseService.getAllWarehouses(pageable,request);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<WarehouseResponse> updateWarehouse(@PathVariable Long id, @Valid @RequestBody WarehouseUpdateRequest request){
        WarehouseResponse response = warehouseService.updateWarehouse(id,request);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<WarehouseResponse> deleteWarehouse(@PathVariable Long id){
        WarehouseResponse response = warehouseService.deleteWarehouse(id);
        return ResponseEntity.ok(response);
    }

}
