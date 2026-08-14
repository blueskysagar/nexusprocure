package com.nexusprocure.purchase.service.Impl;

import com.nexusprocure.authentication.security.CustomUserPrincipal;
import com.nexusprocure.authentication.security.SecurityUtils;
import com.nexusprocure.exception.InvalidPurchaseRequisitionStateException;
import com.nexusprocure.exception.PurchaseRequisitionNotFoundException;
import com.nexusprocure.exception.UserNotFoundException;
import com.nexusprocure.purchase.dto.filter.PurchaseRequisitionFilter;
import com.nexusprocure.purchase.dto.request.PurchaseRequisitionRequest;
import com.nexusprocure.purchase.dto.response.PurchaseRequisitionResponse;
import com.nexusprocure.purchase.entity.PurchaseRequisition;
import com.nexusprocure.purchase.entity.RequisitionStatus;
import com.nexusprocure.purchase.mapper.PurchaseRequisitionMapper;
import com.nexusprocure.purchase.repository.PurchaseRequisitionRepository;
import com.nexusprocure.purchase.service.PurchaseRequisitionService;
import com.nexusprocure.purchase.specification.PurchaseRequisitionSpecification;
import com.nexusprocure.purchase.specification.PurchaseRequisitionSpecificationBuilder;
import com.nexusprocure.user.entity.User;
import com.nexusprocure.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseRequisitionServiceImpl implements PurchaseRequisitionService {
    private final PurchaseRequisitionRepository purchaseRequisitionRepository;
    private final PurchaseRequisitionMapper purchaseRequisitionMapper;


@Override
    public PurchaseRequisitionResponse createPurchaseRequisition(PurchaseRequisitionRequest request){
        PurchaseRequisition purchaseRequisition = purchaseRequisitionMapper.toEntity(request);
    CustomUserPrincipal principal = SecurityUtils.getCurrentUser();
    purchaseRequisition.setRequestBy(principal.getUser());
    purchaseRequisition.setRequisitionNumber(generateRequisitionNumber());
    //Business rule Every newly created Purchase Requisition starts as DRAFT
    purchaseRequisition.setStatus(RequisitionStatus.DRAFT);
    PurchaseRequisition savedPurchaseRequisition = purchaseRequisitionRepository.save(purchaseRequisition);
    return purchaseRequisitionMapper.toResponse(savedPurchaseRequisition);

}

private String generateRequisitionNumber() {
    Optional<PurchaseRequisition> lastRequisition = purchaseRequisitionRepository.findTopByOrderByIdDesc();
    int nextNumber = 1;
    if (lastRequisition.isPresent()) {
        PurchaseRequisition purchaseRequisition = lastRequisition.get();
        String lastNumber = purchaseRequisition.getRequisitionNumber();
        String numericPart = lastNumber.substring(lastNumber.lastIndexOf("-") + 1);
        nextNumber = Integer.parseInt(numericPart) + 1;


    }
    return String.format("PR-%05d", nextNumber);

}
@Override
@Transactional(readOnly = true)
    public Page<PurchaseRequisitionResponse> getAllPurchaseRequisitions(Pageable pageable, PurchaseRequisitionFilter filter){
    Specification<PurchaseRequisition> specification = PurchaseRequisitionSpecificationBuilder.build(filter);
       Page<PurchaseRequisition> purchaseRequisitionPage =  purchaseRequisitionRepository.findAll(specification, pageable);
       return purchaseRequisitionPage.map(purchaseRequisitionMapper::toResponse);

}
@Override
    @Transactional(readOnly = true)
    public PurchaseRequisitionResponse getPurchaseRequisitionById(Long id){
        PurchaseRequisition purchaseRequisition = purchaseRequisitionRepository.findById(id).orElseThrow(()  -> new PurchaseRequisitionNotFoundException(id));
        return purchaseRequisitionMapper.toResponse(purchaseRequisition);
}
@Override
@Transactional
@PreAuthorize("@purchaseRequisitionSecurity.canEdit(#id)")
    public PurchaseRequisitionResponse updatePurchaseRequisition(Long id, PurchaseRequisitionRequest request){
        PurchaseRequisition purchaseRequisition = purchaseRequisitionRepository.findById(id).orElseThrow(() ->
                new PurchaseRequisitionNotFoundException(id));

        purchaseRequisition.setTitle(request.getTitle());
        purchaseRequisition.setDescription(request.getDescription());
        purchaseRequisition.setDepartment(request.getDepartment());
        purchaseRequisition.setRequisitionPriority(request.getPriority());
        purchaseRequisition.setTotalAmount(request.getTotalAmount());
        purchaseRequisition.setRequiredDate(request.getRequiredAt());


    PurchaseRequisition updatedpurchaseRequisition = purchaseRequisitionRepository.save(purchaseRequisition);
        return purchaseRequisitionMapper.toResponse(updatedpurchaseRequisition);

}
@Override
    public PurchaseRequisitionResponse submitPurchaseRequisition(Long id){
    PurchaseRequisition purchaseRequisition = purchaseRequisitionRepository.findById(id).orElseThrow(() ->
            new PurchaseRequisitionNotFoundException(id));
    if(purchaseRequisition.getStatus() != RequisitionStatus.DRAFT){
        throw new InvalidPurchaseRequisitionStateException("Only draft purchase requisitions can be submitted");
    }
    purchaseRequisition.setStatus(RequisitionStatus.PENDING);
    PurchaseRequisition submittedPurchaseRequisition = purchaseRequisitionRepository.save(purchaseRequisition);
    return purchaseRequisitionMapper.toResponse(submittedPurchaseRequisition);
}

@Override
@PreAuthorize("@purchaseRequisitionSecurity.canApprove(#requisitionId)")
    public PurchaseRequisitionResponse approvePurchaseRequisition(Long requisitionId) {
    PurchaseRequisition purchaseRequisition = purchaseRequisitionRepository.findById(requisitionId).orElseThrow(() -> new PurchaseRequisitionNotFoundException(requisitionId));

    if (purchaseRequisition.getStatus() != RequisitionStatus.PENDING) {
        throw new InvalidPurchaseRequisitionStateException("Only pending purchase requisitions can be approved");}
        purchaseRequisition.setStatus(RequisitionStatus.APPROVED);

        PurchaseRequisition approvedPurchaseRequisition =
                purchaseRequisitionRepository.save(purchaseRequisition);
    return purchaseRequisitionMapper.toResponse(approvedPurchaseRequisition);


}

@Override
@PreAuthorize("@purchaseRequisitionSecurity.canReject(#requisitionId)")
    public PurchaseRequisitionResponse rejectPurchaseRequisition(Long id){
    PurchaseRequisition purchaseRequisition = purchaseRequisitionRepository.findById(id).orElseThrow(() ->
            new PurchaseRequisitionNotFoundException(id));
    if(purchaseRequisition.getStatus() != RequisitionStatus.PENDING){
        throw  new InvalidPurchaseRequisitionStateException("Only pending purchase requisitions can be rejected");
    }
    purchaseRequisition.setStatus(RequisitionStatus.REJECTED);
    PurchaseRequisition rejectedPurchaseRequisition = purchaseRequisitionRepository.save(purchaseRequisition);
    return purchaseRequisitionMapper.toResponse(rejectedPurchaseRequisition);
}
@Override
@PreAuthorize("@purchaseRequisitionSecurity.canCancel(#id)")
    public PurchaseRequisitionResponse cancelPurchaseRequisition(Long id){
    PurchaseRequisition purchaseRequisition = purchaseRequisitionRepository.findById(id).orElseThrow(() ->
            new PurchaseRequisitionNotFoundException(id));
    if(purchaseRequisition.getStatus() != RequisitionStatus.DRAFT && purchaseRequisition.getStatus() != RequisitionStatus.PENDING){
        throw new InvalidPurchaseRequisitionStateException("only draft or pending requisitions can be cancelled");
    }
    purchaseRequisition.setStatus(RequisitionStatus.CANCELLED);
    PurchaseRequisition cancelledPurchaseRequisition = purchaseRequisitionRepository.save(purchaseRequisition);
    return purchaseRequisitionMapper.toResponse(cancelledPurchaseRequisition);
}
}
