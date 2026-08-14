package com.nexusprocure.purchaseorder.controller;

import com.nexusprocure.purchaseorder.dto.Filter.PurchaseOrderFilterRequest;
import com.nexusprocure.purchaseorder.dto.Request.PurchaseOrderRequest;
import com.nexusprocure.purchaseorder.dto.Request.PurchaseOrderUpdateRequest;
import com.nexusprocure.purchaseorder.dto.Response.PurchaseOrderResponse;
import com.nexusprocure.purchaseorder.service.PurchaseOrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;
    public PurchaseOrderController(PurchaseOrderService purchaseOrderService){
        this.purchaseOrderService = purchaseOrderService;
    }
    @PostMapping
    public ResponseEntity<PurchaseOrderResponse> createPurchaseOrder(@RequestBody @Valid PurchaseOrderRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(purchaseOrderService.createPurchaseOrder(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderResponse> getPurchaseOrderById(@PathVariable Long id){
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrderById(id));

    }
    @GetMapping
    public ResponseEntity<Page<PurchaseOrderResponse>> getPurchaseOrders(@ModelAttribute PurchaseOrderFilterRequest request, Pageable pageable){
        Page<PurchaseOrderResponse> response =
                purchaseOrderService.getPurchaseOrders(request, pageable);
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrders(request,pageable));
    }
    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrderResponse> updatePurchaseOrder(@PathVariable Long id, @RequestBody @Valid PurchaseOrderUpdateRequest request){
        return ResponseEntity.ok(purchaseOrderService.updatePurchaseOrder(id, request));
    }
    @PatchMapping("/{id}/approve")
    public ResponseEntity<PurchaseOrderResponse> approvePurchaseOrder(@PathVariable Long id){
        return ResponseEntity.ok(purchaseOrderService.approvePurchaseOrder(id));
    }
    @PatchMapping("/{id}/send")
    public ResponseEntity<PurchaseOrderResponse> sendPurchaseOrder(@PathVariable Long id){
        return ResponseEntity.ok(purchaseOrderService.sendPurchaseOrder(id));
    }
    @PatchMapping("/{id}/complete")
    public ResponseEntity<PurchaseOrderResponse> completePurchaseOrder(@PathVariable Long id){
        return ResponseEntity.ok(purchaseOrderService.completePurchaseOrder(id));
    }
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<PurchaseOrderResponse> cancelPurchaseOrder(@PathVariable Long id){
        return ResponseEntity.ok(purchaseOrderService.cancelPurchaseOrder(id));
    }


}
