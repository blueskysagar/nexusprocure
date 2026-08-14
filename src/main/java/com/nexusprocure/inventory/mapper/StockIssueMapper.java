package com.nexusprocure.inventory.mapper;

import com.nexusprocure.inventory.dto.response.StockIssueItemResponse;
import com.nexusprocure.inventory.dto.response.StockIssueResponse;
import com.nexusprocure.inventory.entity.StockIssue;
import com.nexusprocure.inventory.entity.StockIssueItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockIssueMapper {
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "warehouseName", source = "warehouse.name")
    @Mapping(target = "requestedById", source = "requestedBy.id")
    @Mapping(target = "approvedById", source = "approvedBy.id")
    @Mapping(target = "requestedByName", source = "requestedBy.fullName")
    @Mapping(target = "approvedByName", source = "approvedBy.fullName")
    StockIssueResponse toResponse(StockIssue stockIssue);
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    StockIssueItemResponse toResponse(StockIssueItem stockIssueItem);
}
