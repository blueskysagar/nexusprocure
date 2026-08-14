package com.nexusprocure.product.mapper;

import com.nexusprocure.product.dto.request.ProductRequest;
import com.nexusprocure.product.dto.response.ProductResponse;
import com.nexusprocure.product.dto.update.ProductUpdateRequest;
import com.nexusprocure.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productCode", ignore = true)
    @Mapping(target = "productStatus", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductRequest request);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productCode", ignore = true)
    @Mapping(target = "productStatus", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntity(
            ProductUpdateRequest request,
            @MappingTarget Product product);
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    ProductResponse toResponse(Product product);

}
