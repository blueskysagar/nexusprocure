package com.nexusprocure.product.service;

import com.nexusprocure.product.dto.request.CategoryRequest;
import com.nexusprocure.product.dto.request.ProductFilterRequest;
import com.nexusprocure.product.dto.update.CategoryUpdateRequest;
import com.nexusprocure.product.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse getCategoryById(Long id);
    Page<CategoryResponse> getAllCategories (Pageable pageable);
    CategoryResponse updateCategory(Long id, CategoryUpdateRequest request);
    void deactivateCategory(Long id);
    void activateCategory(Long id);
}
