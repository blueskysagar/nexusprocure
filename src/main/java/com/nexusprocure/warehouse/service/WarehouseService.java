package com.nexusprocure.warehouse.service;

import com.nexusprocure.warehouse.dto.Filter.WarehouseFilterRequest;
import com.nexusprocure.warehouse.dto.Request.WarehouseRequest;
import com.nexusprocure.warehouse.dto.Response.WarehouseResponse;
import com.nexusprocure.warehouse.dto.Request.WarehouseUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WarehouseService {
    WarehouseResponse createWarehouse(WarehouseRequest request);
    WarehouseResponse getWarehouseById(Long id);
    Page<WarehouseResponse> getAllWarehouses(Pageable pageable, WarehouseFilterRequest request);
    WarehouseResponse updateWarehouse(Long id, WarehouseUpdateRequest request);
    WarehouseResponse deleteWarehouse(Long id);
}
