package com.nexusprocure.purchase.controller;

import com.nexusprocure.purchase.dto.filter.PurchaseRequisitionFilter;
import com.nexusprocure.purchase.dto.request.PurchaseRequisitionRequest;
import com.nexusprocure.purchase.dto.response.PurchaseRequisitionResponse;
import com.nexusprocure.purchase.entity.PurchaseRequisition;
import com.nexusprocure.purchase.entity.RequisitionStatus;
import com.nexusprocure.purchase.service.PurchaseRequisitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-requisitions")
@RequiredArgsConstructor
public class PurchaseRequisitionController {
    private final PurchaseRequisitionService purchaseRequisitionService;
    @PostMapping
    public ResponseEntity<PurchaseRequisitionResponse> createPurchaseRequisition(@Valid @RequestBody PurchaseRequisitionRequest request){
        PurchaseRequisitionResponse purchaseRequisitionResponse = purchaseRequisitionService.createPurchaseRequisition(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseRequisitionResponse);
    }
   @GetMapping
    public ResponseEntity<Page<PurchaseRequisitionResponse>> getAllPurchaseRequisitions(@PageableDefault(page = 0, size = 10) Pageable pageable, PurchaseRequisitionFilter filter){
        Page<PurchaseRequisitionResponse> purchaseRequisitionResponsePage = purchaseRequisitionService.getAllPurchaseRequisitions(pageable, filter);
        return ResponseEntity.ok(purchaseRequisitionResponsePage);
   }
   @GetMapping("/{id}")
    public ResponseEntity<PurchaseRequisitionResponse> getPurchaseRequisitionById(@PathVariable Long id){
        PurchaseRequisitionResponse purchaseRequisitionResponse = purchaseRequisitionService.getPurchaseRequisitionById(id);
        return ResponseEntity.ok(purchaseRequisitionResponse);
   }

     @PutMapping("/{id}")
    public ResponseEntity<PurchaseRequisitionResponse> updatePurchaseRequisition(@PathVariable Long id, @Valid @RequestBody PurchaseRequisitionRequest request){
        PurchaseRequisitionResponse purchaseRequisitionResponse = purchaseRequisitionService.updatePurchaseRequisition(id, request);
        return ResponseEntity.ok(purchaseRequisitionResponse);
     }
     @PostMapping("/{id}/submit")
    public ResponseEntity<PurchaseRequisitionResponse> submitPurchaseRequisition(@PathVariable Long id){
        PurchaseRequisitionResponse purchaseRequisitionResponse = purchaseRequisitionService.submitPurchaseRequisition(id);
        return ResponseEntity.ok(purchaseRequisitionResponse);
     }
     @PostMapping("/{id}/approve")

    public ResponseEntity<PurchaseRequisitionResponse> approvePurchaseRequisition(@PathVariable Long id){
        PurchaseRequisitionResponse purchaseRequisitionResponse = purchaseRequisitionService.approvePurchaseRequisition(id);
        return ResponseEntity.ok(purchaseRequisitionResponse);
     }
     @PostMapping("/{id}/reject")

    public ResponseEntity<PurchaseRequisitionResponse> rejectPurchaseRequisition(@PathVariable Long id){
        PurchaseRequisitionResponse purchaseRequisitionResponse = purchaseRequisitionService.rejectPurchaseRequisition(id);
        return ResponseEntity.ok(purchaseRequisitionResponse);
     }
    @PostMapping("/{id}/cancel")
    public ResponseEntity<PurchaseRequisitionResponse> cancelPurchaseRequisition(@PathVariable Long id){
        PurchaseRequisitionResponse purchaseRequisitionResponse = purchaseRequisitionService.cancelPurchaseRequisition(id);
        return ResponseEntity.ok(purchaseRequisitionResponse);
    }


}
