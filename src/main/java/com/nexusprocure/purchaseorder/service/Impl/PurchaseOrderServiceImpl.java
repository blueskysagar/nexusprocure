package com.nexusprocure.purchaseorder.service.Impl;

import com.nexusprocure.common.event.purchaseorder.PurchaseOrderApprovedApplicationEvent;
import com.nexusprocure.common.event.purchaseorder.PurchaseOrderApprovedEvent;
import com.nexusprocure.common.event.purchaseorder.PurchaseOrderItemEvent;
import com.nexusprocure.common.kafka.PurchaseOrderEventPublisher;
import com.nexusprocure.exception.*;
import com.nexusprocure.purchase.entity.PurchaseRequisition;
import com.nexusprocure.purchase.entity.PurchaseRequisitionItem;
import com.nexusprocure.purchase.entity.RequisitionStatus;
import com.nexusprocure.purchase.repository.PurchaseRequisitionRepository;
import com.nexusprocure.purchaseorder.dto.Filter.PurchaseOrderFilterRequest;
import com.nexusprocure.purchaseorder.dto.Request.PurchaseOrderUpdateRequest;
import com.nexusprocure.purchaseorder.entity.PurchaseOrder;
import com.nexusprocure.purchaseorder.entity.PurchaseOrderItem;
import com.nexusprocure.purchaseorder.entity.PurchaseOrderStatus;
import com.nexusprocure.purchaseorder.repository.PurchaseOrderRepository;
import com.nexusprocure.purchaseorder.dto.Request.PurchaseOrderRequest;
import com.nexusprocure.purchaseorder.dto.Response.PurchaseOrderResponse;
import com.nexusprocure.purchaseorder.mapper.PurchaseOrderMapper;
import com.nexusprocure.purchaseorder.service.PurchaseOrderService;
import com.nexusprocure.purchaseorder.specification.PurchaseOrderSpecificationBuilder;
import com.nexusprocure.vendor.entity.Vendor;
import com.nexusprocure.vendor.repository.VendorRepository;
import com.nexusprocure.warehouse.entity.Warehouse;
import com.nexusprocure.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    private final WarehouseRepository warehouseRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseRequisitionRepository purchaseRequisitionRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final VendorRepository vendorRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @PreAuthorize("@purchaseOrderSecurity.canCreate()")
    // find pr
    public PurchaseOrderResponse createPurchaseOrder(PurchaseOrderRequest request) {
        PurchaseRequisition purchaseRequisition = purchaseRequisitionRepository.findById(request.getPurchaseRequisitionId()).orElseThrow(() ->
                new PurchaseRequisitionNotFoundException(
                        request.getPurchaseRequisitionId()
                ));
        //find vendor
        Vendor vendor = vendorRepository.findById(request.getVendorId()).orElseThrow(() ->
                new VendorNotFoundException(
                        request.getVendorId()
                )
        );
        //find warehouse
        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId()).orElseThrow(() ->
                        new WarehouseNotFoundException(
                                request.getWarehouseId()
                        )
        );
        //check pr status
        if (purchaseRequisition.getStatus() != RequisitionStatus.APPROVED) {
            throw new InvalidPurchaseRequisitionStateException("Only Approved purchase requisitions can create purchase orders");
        }
        // Check duplicate PO
        boolean exists = purchaseOrderRepository.existsByPurchaseRequisition(purchaseRequisition);
        if (exists) {
            throw new InvalidPurchaseOrderStateException("Purchase Order Exists already for this purchase Requisitions");
        }
        // Generate Purchase Order Number
        String purchaseOrderNumber = generatePurchaseOrderNumber();
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .purchaseOrderNumber(purchaseOrderNumber)
                .purchaseRequisition(purchaseRequisition)
                .vendor(vendor)
                .purchaseOrderStatus(PurchaseOrderStatus.CREATED)
                .orderDate(request.getOrderDate())
                .totalAmount(request.getTotalAmount())
                .build();
        for(PurchaseRequisitionItem requisitionItem : purchaseRequisition.getItems()){
            PurchaseOrderItem purchaseOrderItem = PurchaseOrderItem.builder()
                    .product(requisitionItem.getProduct())
                    .warehouse(warehouse)
                    .quantity(requisitionItem.getQuantity())
                    .unitPrice(requisitionItem.getEstimatedUnitPrice())
                    .build();
            purchaseOrder.addItem(purchaseOrderItem);
        }
// save purchase order
        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);
        // Return  response
        return purchaseOrderMapper.toResponse(savedPurchaseOrder);

    }
    private String generatePurchaseOrderNumber(){
        PurchaseOrder lastPurchaseOrder = purchaseOrderRepository.findTopByOrderByIdDesc().orElse(null);
        if(lastPurchaseOrder == null){
            return "PO-00001";
        }
        String lastNumber = lastPurchaseOrder.getPurchaseOrderNumber();
        String numericPart = lastNumber.substring(3);
        int nextNumber = Integer.parseInt(numericPart) + 1;
        return String.format("PO-%05d", nextNumber);


    }
    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("@purchaseOrderSecurity.canRead(#id)")
    public PurchaseOrderResponse getPurchaseOrderById(Long id){
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id).orElseThrow(() ->
                new PurchaseOrderNotFoundException(id));
        return purchaseOrderMapper.toResponse(purchaseOrder);
    }
    @Override
    @PreAuthorize("@purchaseOrderSecurity.canUpdate(#id")
    @Transactional
    public PurchaseOrderResponse updatePurchaseOrder(Long id, PurchaseOrderUpdateRequest request){
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id).orElseThrow(() ->
                new PurchaseOrderNotFoundException(id));
        if(purchaseOrder.getPurchaseOrderStatus() !=PurchaseOrderStatus.CREATED){
            throw new InvalidPurchaseOrderStateException("Only purchase orders in created status can be updated");
        }
        Vendor vendor = vendorRepository.findById(request.getVendorId()).orElseThrow(() ->
                new VendorNotFoundException(request.getVendorId()));
        purchaseOrder.setVendor(vendor);
        purchaseOrder.setOrderDate(request.getOrderDate());
        purchaseOrder.setTotalAmount(request.getTotalAmount());
        return purchaseOrderMapper.toResponse(purchaseOrder);

    }
    @Override
    @PreAuthorize("@purchaseOrderSecurity.canApprove(#id)")
    @Transactional
    public PurchaseOrderResponse approvePurchaseOrder(Long id){
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id).orElseThrow(() ->
                new PurchaseOrderNotFoundException(id)
        );
        PurchaseOrderStatus status = purchaseOrder.getPurchaseOrderStatus();
        if(status !=PurchaseOrderStatus.CREATED){
            throw new InvalidPurchaseOrderStateException(
                    "Only CREATED Purchase Orders can be approved."
            );
        }
        purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.APPROVED);
        List<PurchaseOrderItemEvent> items = purchaseOrder.getItems()
                .stream()
                .map(item ->
                                new PurchaseOrderItemEvent(

                                        item.getProduct().getId(),

                                        item.getWarehouse().getId(),

                                        item.getQuantity(),

                                        item.getUnitPrice()

                                )
                )
                .toList();
        PurchaseOrderApprovedEvent event = new PurchaseOrderApprovedEvent(purchaseOrder.getId(),
                purchaseOrder.getPurchaseOrderNumber(),
                purchaseOrder.getVendor().getId(),
                purchaseOrder.getTotalAmount(),
                LocalDateTime.now(),
                items);
        applicationEventPublisher.publishEvent(new PurchaseOrderApprovedApplicationEvent(event));
        return purchaseOrderMapper.toResponse(purchaseOrder);

    }
    @Override
    @PreAuthorize("@purchaseOrderSecurity.canSend(#id)")
    @Transactional
    public PurchaseOrderResponse sendPurchaseOrder(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id).orElseThrow(() ->
                new PurchaseOrderNotFoundException(id)
        );
        PurchaseOrderStatus status = purchaseOrder.getPurchaseOrderStatus();
        if (status != PurchaseOrderStatus.APPROVED) {
            throw new InvalidPurchaseOrderStateException(
                    "Only APPROVED Purchase Orders can be sent."
            );
        }
            purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.SENT);
            return purchaseOrderMapper.toResponse(purchaseOrder);

    }
    @Override
    @PreAuthorize("@purchaseOrderSecurity.canComplete(#id)")
    @Transactional
    public PurchaseOrderResponse completePurchaseOrder(Long id)
        {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id).orElseThrow(() ->
                new PurchaseOrderNotFoundException(id)
        );
        PurchaseOrderStatus status = purchaseOrder.getPurchaseOrderStatus();
        if(status !=PurchaseOrderStatus.SENT){
            throw new InvalidPurchaseOrderStateException(
                    "Only SENT Purchase Orders can be completed."
            );
        }
        purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.COMPLETED);
        return purchaseOrderMapper.toResponse(purchaseOrder);

    }


@Override
@PreAuthorize("@purchaseOrderSecurity.canCancel(#id)")
@Transactional
public PurchaseOrderResponse cancelPurchaseOrder(Long id){
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id).orElseThrow(() ->
                new PurchaseOrderNotFoundException(id));
        if(purchaseOrder.getPurchaseOrderStatus() !=PurchaseOrderStatus.CREATED && purchaseOrder.getPurchaseOrderStatus() !=PurchaseOrderStatus.APPROVED){
            throw new InvalidPurchaseOrderStateException("Only Created or approved Purchase Order can be cancelled");
        }
         purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.CANCELLED);
        return purchaseOrderMapper.toResponse(purchaseOrder);
        }

    @Override
    @PreAuthorize("@purchaseOrderSecurity.canReadAll()")
    @Transactional(readOnly = true)
    public Page<PurchaseOrderResponse> getPurchaseOrders(PurchaseOrderFilterRequest request, Pageable pageable){
        Specification<PurchaseOrder> specification = PurchaseOrderSpecificationBuilder.build(request);
        Page<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll(specification, pageable);
        return purchaseOrders.map(purchaseOrderMapper::toResponse);
    }










}
