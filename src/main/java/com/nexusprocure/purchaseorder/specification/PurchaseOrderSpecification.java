package com.nexusprocure.purchaseorder.specification;

import com.nexusprocure.purchase.entity.PurchaseRequisition;
import com.nexusprocure.purchaseorder.entity.PurchaseOrder;
import com.nexusprocure.purchaseorder.entity.PurchaseOrderStatus;
import com.nexusprocure.vendor.entity.Vendor;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class PurchaseOrderSpecification {
    public static Specification<PurchaseOrder> hasStatus(PurchaseOrderStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("purchaseOrderStatus"), status);
        };

    }

    public static Specification<PurchaseOrder> hasVendor(Long vendorId) {
        return (root, query, criteriaBuilder) -> {
            Join<PurchaseOrder, Vendor> vendorJoin = root.join("vendor");

            return criteriaBuilder.equal(vendorJoin.get("id"), vendorId);
        };

    }

    public static Specification<PurchaseOrder> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {

            return criteriaBuilder.like(criteriaBuilder.lower(root.get("purchaseOrderNumber")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<PurchaseOrder> hasDateRange(LocalDate fromDate, LocalDate toDate) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (fromDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("orderDate"), fromDate));
            }
            if (toDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("orderDate"), toDate));
            }
            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

    }

    public static Specification<PurchaseOrder> hasAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (minAmount != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("totalAmount"), minAmount));
            }
            if (maxAmount != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("totalAmount"), maxAmount));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<PurchaseOrder> hasRequisitionNumber(String requisitionNumber)
    {
          return (root, query, criteriaBuilder) -> {
              Join<PurchaseOrder, PurchaseRequisition> requisitionJoin = root.join("purchaseRequisition");
              return criteriaBuilder.equal(requisitionJoin.get("requisitionNumber"),requisitionNumber);
          };

    }
}


