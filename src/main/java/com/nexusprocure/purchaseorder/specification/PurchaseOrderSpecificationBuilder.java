package com.nexusprocure.purchaseorder.specification;

import com.nexusprocure.purchaseorder.dto.Filter.PurchaseOrderFilterRequest;
import com.nexusprocure.purchaseorder.entity.PurchaseOrder;
import org.springframework.data.jpa.domain.Specification;

public class PurchaseOrderSpecificationBuilder {
    public static Specification<PurchaseOrder> build(PurchaseOrderFilterRequest request){
        Specification<PurchaseOrder> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        if(request.getStatus() !=null){
            specification = specification.and(PurchaseOrderSpecification.hasStatus(request.getStatus()));
        }
        if(request.getVendorId() !=null){
            specification = specification.and(PurchaseOrderSpecification.hasVendor(request.getVendorId()));
        }
        if(request.getKeyword() !=null && request.getKeyword().isBlank()){
            specification = specification.and(PurchaseOrderSpecification.hasKeyword(request.getKeyword()));
        }
        if(request.getFromDate() !=null || request.getToDate() !=null){
            specification = specification.and(PurchaseOrderSpecification.hasDateRange(request.getFromDate(), request.getToDate()));
        }
        if(request.getMinAmount() !=null && request.getMaxAmount() !=null){
            specification = specification.and(PurchaseOrderSpecification.hasAmountRange(request.getMinAmount(), request.getMaxAmount()));
        }
        return specification;
    }
}
