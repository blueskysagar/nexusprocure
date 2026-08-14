package com.nexusprocure.purchase.specification;

import com.nexusprocure.purchase.dto.filter.PurchaseRequisitionFilter;
import com.nexusprocure.purchase.entity.PurchaseRequisition;
import org.springframework.data.jpa.domain.Specification;

public class PurchaseRequisitionSpecificationBuilder {
    public static Specification<PurchaseRequisition> build(PurchaseRequisitionFilter filter) {
        Specification<PurchaseRequisition> specification = ((root, query, criteriaBuilder) -> null);
            specification = specification.and(PurchaseRequisitionSpecification.hasStatus(filter.getStatus()));
            specification = specification.and(PurchaseRequisitionSpecification.hasDepartment(filter.getDepartment()));
            specification = specification.and(PurchaseRequisitionSpecification.hasRequester(filter.getRequesterId()));

             specification = specification.and(PurchaseRequisitionSpecification.hasPriority(filter.getPriority()));
             specification = specification.and(PurchaseRequisitionSpecification.requiredDateFrom(filter.getRequiredDateFrom()));
             specification = specification.and(PurchaseRequisitionSpecification.requiredDateTo(filter.getRequiredDateTo()));
             specification = specification.and(PurchaseRequisitionSpecification.minimumAmount(filter.getMinAmount()));
             specification = specification.and(PurchaseRequisitionSpecification.maximumAmount(filter.getMaxAmount()));

             specification = specification.and(PurchaseRequisitionSpecification.hasKeyword(filter.getKeyword()));
        return specification;

    }

}
