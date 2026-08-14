package com.nexusprocure.purchaseorder.mapper;

import com.nexusprocure.purchaseorder.dto.Response.PurchaseOrderResponse;
import com.nexusprocure.purchaseorder.entity.PurchaseOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {
    @Mapping(target = "vendorName", source = "vendor.vendorName")
    @Mapping(target = "purchaseRequisitionNumber", source = "purchaseRequisition.requisitionNumber")
    PurchaseOrderResponse toResponse(PurchaseOrder purchaseOrder);
}
