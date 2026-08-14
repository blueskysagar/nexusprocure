package com.nexusprocure.purchase.specification;

import com.nexusprocure.purchase.entity.PurchaseRequisition;
import com.nexusprocure.purchase.entity.RequisitionPriority;
import com.nexusprocure.purchase.entity.RequisitionStatus;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PurchaseRequisitionSpecification {
    public static Specification<PurchaseRequisition> hasStatus(RequisitionStatus status) {
        return (root, query, criteriaBuilder) -> status == null ? null : criteriaBuilder.equal(root.get("status"), status);

    }

    public static Specification<PurchaseRequisition> hasDepartment(String department) {
        return (root, query, criteriaBuilder) -> department == null || department.isBlank() ? null : criteriaBuilder.equal(root.get("department"), department);
    }

    public static Specification<PurchaseRequisition> hasRequester(Long requesterId) {
        return ((root, query, criteriaBuilder) -> requesterId == null ? null : criteriaBuilder.equal(root.get("requestBy").get("id"), requesterId));

    }

    public static Specification<PurchaseRequisition> hasPriority(RequisitionPriority priority) {
        return ((root, query, criteriaBuilder) -> priority == null ? null : criteriaBuilder.equal(root.get("requisitionPriority"), priority));
    }

    public static Specification<PurchaseRequisition> requiredDateFrom(LocalDate requiredDateFrom) {
        return ((root, query, criteriaBuilder) -> requiredDateFrom == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("requiredDate"), requiredDateFrom));
    }

    public static Specification<PurchaseRequisition> requiredDateTo(LocalDate requiredDateTo) {
        return ((root, query, criteriaBuilder) -> requiredDateTo == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("requiredDate"), requiredDateTo));
    }

    public static Specification<PurchaseRequisition> minimumAmount(BigDecimal minAmount) {
        return (root, query, criteriaBuilder) -> minAmount == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("totalAmount"), minAmount);

    }

    public static Specification<PurchaseRequisition> maximumAmount(BigDecimal maxAmount) {
        return ((root, query, criteriaBuilder) -> maxAmount == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("totalAmount"), maxAmount));
    }

    public static Specification<PurchaseRequisition> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) ->
        {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }
            String searchKeyword = "%" + keyword.toLowerCase() + "%";
            var requesterJoin = root.join("requestBy");
            var titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), searchKeyword);
            var descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchKeyword);
            var departmentPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("department")), searchKeyword);
            var requisitionNumberPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("requisitionNumber")), searchKeyword);
            var firstNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(requesterJoin.get("firstName")), searchKeyword);
            var lastNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(requesterJoin.get("lastName")), searchKeyword);
            return criteriaBuilder.or(
                    titlePredicate,
                    descriptionPredicate,
                    departmentPredicate,
                    requisitionNumberPredicate,
                    firstNamePredicate,
                    lastNamePredicate
            );
        };
    }
}
