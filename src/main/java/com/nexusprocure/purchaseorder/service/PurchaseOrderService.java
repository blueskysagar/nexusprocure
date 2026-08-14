package com.nexusprocure.purchaseorder.service;

import com.nexusprocure.purchaseorder.dto.Filter.PurchaseOrderFilterRequest;
import com.nexusprocure.purchaseorder.dto.Request.PurchaseOrderRequest;
import com.nexusprocure.purchaseorder.dto.Request.PurchaseOrderUpdateRequest;
import com.nexusprocure.purchaseorder.dto.Response.PurchaseOrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PurchaseOrderService {
    PurchaseOrderResponse createPurchaseOrder(PurchaseOrderRequest request);
    PurchaseOrderResponse getPurchaseOrderById(Long id);
    PurchaseOrderResponse updatePurchaseOrder(Long id, PurchaseOrderUpdateRequest request);
    PurchaseOrderResponse approvePurchaseOrder(Long id);
    PurchaseOrderResponse sendPurchaseOrder(Long id);
    PurchaseOrderResponse completePurchaseOrder(Long id);
    PurchaseOrderResponse cancelPurchaseOrder(Long id);
    Page<PurchaseOrderResponse> getPurchaseOrders(PurchaseOrderFilterRequest request, Pageable pageable);


}
