package com.nexusprocure.purchase.mapper;

import com.nexusprocure.purchase.dto.request.PurchaseRequisitionRequest;
import com.nexusprocure.purchase.dto.response.PurchaseRequisitionResponse;
import com.nexusprocure.purchase.entity.PurchaseRequisition;
import com.nexusprocure.user.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface PurchaseRequisitionMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "department", source = "department")
    @Mapping(target = "requisitionPriority", source = "priority")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "requiredDate", source = "requiredAt")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requisitionNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "requestBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PurchaseRequisition toEntity(PurchaseRequisitionRequest request);
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "requisitionNumber", source = "requisitionNumber")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "department", source = "department")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "requiredDate", source = "requiredDate")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "requestedById", source = "requestBy.id")
    @Mapping(target = "requestedByName", source = "requestBy")
    PurchaseRequisitionResponse toResponse(PurchaseRequisition entity);

    List<PurchaseRequisitionResponse> toResponseList(List<PurchaseRequisition> entities);
    default String mapRequestedByName(User user) {
        if (user == null) {
            return null;
        }

        return user.getFirstName() + " " + user.getLastName();
    }
}
