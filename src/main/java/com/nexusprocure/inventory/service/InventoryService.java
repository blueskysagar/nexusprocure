package com.nexusprocure.inventory.service;

import com.nexusprocure.common.Response.PageResponse;
import com.nexusprocure.common.event.purchaseorder.PurchaseOrderApprovedEvent;
import com.nexusprocure.inventory.dto.request.InventoryFilterRequest;
import com.nexusprocure.inventory.dto.request.InventoryRequest;
import com.nexusprocure.inventory.dto.response.InventoryResponse;
import com.nexusprocure.inventory.dto.update.InventoryUpdateRequest;
import org.springframework.data.domain.Pageable;

public interface InventoryService {
    InventoryResponse createInventory(InventoryRequest request);
    InventoryResponse getInventoryById(Long id);
    PageResponse<InventoryResponse> getAllInventories(InventoryFilterRequest request, Pageable pageable);
    InventoryResponse updateInventory(Long id, InventoryUpdateRequest request);
    void activateInventory(Long id);
    void deactivateInventory(Long id);

}
