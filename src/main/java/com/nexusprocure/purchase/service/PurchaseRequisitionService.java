package com.nexusprocure.purchase.service;

import com.nexusprocure.purchase.dto.filter.PurchaseRequisitionFilter;
import com.nexusprocure.purchase.dto.request.PurchaseRequisitionRequest;
import com.nexusprocure.purchase.dto.response.PurchaseRequisitionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


public interface PurchaseRequisitionService {
    PurchaseRequisitionResponse createPurchaseRequisition(PurchaseRequisitionRequest request);
    Page<PurchaseRequisitionResponse> getAllPurchaseRequisitions(Pageable pageable, PurchaseRequisitionFilter filter);
    PurchaseRequisitionResponse getPurchaseRequisitionById(Long id);
    PurchaseRequisitionResponse updatePurchaseRequisition(Long id, PurchaseRequisitionRequest request);
    PurchaseRequisitionResponse submitPurchaseRequisition(Long id);
    PurchaseRequisitionResponse approvePurchaseRequisition(Long id);
    PurchaseRequisitionResponse rejectPurchaseRequisition(Long id);
    PurchaseRequisitionResponse cancelPurchaseRequisition(Long id);



}
