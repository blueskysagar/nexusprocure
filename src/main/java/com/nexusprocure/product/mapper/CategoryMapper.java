package com.nexusprocure.product.mapper;

import com.nexusprocure.product.dto.request.CategoryRequest;
import com.nexusprocure.product.dto.update.CategoryUpdateRequest;
import com.nexusprocure.product.dto.response.CategoryResponse;
import com.nexusprocure.product.entity.Category;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @BeanMapping(ignoreByDefault = true)// it says dont map all the fields
    @Mapping(target = "name", source = "name")// source is dto request and target is entity
    @Mapping(target = "description", source = "description")
    Category toEntity(CategoryRequest request);
    void updateEntity(CategoryUpdateRequest request, @MappingTarget Category category);
    // @Mapping Target dont have to create objects it just sets the required value automatically


    @BeanMapping(ignoreByDefault = true)// this whole @Mapping is done because we just
    //want to do explicit mapping as we dont want the client to see all the details.
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    CategoryResponse toResponse(Category category);
    List<CategoryResponse> toResponseList(List<Category> categories);
}
