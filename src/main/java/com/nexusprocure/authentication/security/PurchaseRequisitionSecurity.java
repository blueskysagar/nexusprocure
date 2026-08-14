package com.nexusprocure.authentication.security;

import com.nexusprocure.exception.PurchaseRequisitionNotFoundException;
import com.nexusprocure.purchase.entity.PurchaseRequisition;
import com.nexusprocure.purchase.repository.PurchaseRequisitionRepository;
import com.nexusprocure.user.entity.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("purchaseRequisitionSecurity")
public class PurchaseRequisitionSecurity {
    private final PurchaseRequisitionRepository purchaseRequisitionRepository;

    public PurchaseRequisitionSecurity(PurchaseRequisitionRepository purchaseRequisitionRepository){
        this.purchaseRequisitionRepository = purchaseRequisitionRepository;
    }
    public boolean canEdit(Long requisitionId) {

        PurchaseRequisition purchaseRequisition =
                getPurchaseRequisition(requisitionId);

        if (hasElevatedRole()) {
            return true;
        }

        return isOwner(purchaseRequisition);
    }

    public boolean canCancel(Long requisitionId) {

        PurchaseRequisition purchaseRequisition =
                getPurchaseRequisition(requisitionId);

        if (hasElevatedRole()) {
            return true;
        }

        return isOwner(purchaseRequisition);
    }

    public boolean canApprove(Long requisitionId) {
        return hasElevatedRole();
    }

    public boolean canReject(Long requisitionId) {
        return hasElevatedRole();
    }

    /**
     * Returns true if the current user is ADMIN or MANAGER.
     */
    private boolean hasElevatedRole() {
        return SecurityUtils.hasAnyRole(Role.ADMIN, Role.MANAGER);

    }

    /**
     * Returns true if the current user owns the requisition.
     */
    private boolean isOwner(PurchaseRequisition purchaseRequisition) {

        CustomUserPrincipal principal =
                SecurityUtils.getCurrentUser();

        return purchaseRequisition
                .getRequestBy()
                .getId()
                .equals(principal.getUser().getId());
    }

    /**
     * Loads the Purchase Requisition or throws an exception.
     */
    private PurchaseRequisition getPurchaseRequisition(Long requisitionId) {

        return purchaseRequisitionRepository.findById(requisitionId)
                .orElseThrow(() ->
                        new PurchaseRequisitionNotFoundException(requisitionId));
    }
}
