package com.nexusprocure.product.service.impl;

import com.nexusprocure.exception.DuplicateResourceException;
import com.nexusprocure.exception.InvalidOperationException;
import com.nexusprocure.exception.ResourceNotFoundException;
import com.nexusprocure.product.dto.request.CategoryRequest;
import com.nexusprocure.product.dto.update.CategoryUpdateRequest;
import com.nexusprocure.product.dto.response.CategoryResponse;
import com.nexusprocure.product.entity.Category;
import com.nexusprocure.product.enums.CategoryStatus;
import com.nexusprocure.product.mapper.CategoryMapper;
import com.nexusprocure.product.repository.CategoryRepository;
import com.nexusprocure.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    @Override
    public CategoryResponse createCategory(CategoryRequest request){
        if(categoryRepository.existsByNameIgnoreCase(request.getName())){
            throw new DuplicateResourceException("Category already exists: " + request.getName());
        }
        Category category = categoryMapper.toEntity(request);
        category.setStatus(CategoryStatus.ACTIVE);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);

    }
    @Override
    public CategoryResponse getCategoryById(Long id){
        Category category = getCategoryOrThrow(id);
        return categoryMapper.toResponse(category);
    }
    @Override
    public Page< CategoryResponse> getAllCategories(Pageable pageable){
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(categoryMapper::toResponse);
    }
   @Override
    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest request){
        Category category = getCategoryOrThrow(id);
        if(!category.getName().equalsIgnoreCase(request.getName()) && categoryRepository.existsByNameIgnoreCase(request.getName())){
            throw new DuplicateResourceException(
                    "Category already exists: "
                            + request.getName()
            );
        }
//        category.setName(request.getName()); you see we are setting manually lets say if there are many fields in future
//        category.setDescription(request.getDescription());

       categoryMapper.updateEntity(request, category);// above line is replaced by one
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(updatedCategory);
   }
   @Override
    public void deactivateCategory(Long id){
        Category category = getCategoryOrThrow(id);
        if(category.getStatus() == CategoryStatus.INACTIVE){
            throw new InvalidOperationException("Category is already inactive.");

            }
        category.setStatus(CategoryStatus.INACTIVE);
        categoryRepository.save(category);
        }
        @Override
    public void activateCategory(Long id){
        Category category = getCategoryOrThrow(id);
        if(category.getStatus() == CategoryStatus.ACTIVE){
            throw new InvalidOperationException("Category is already active");
        }
        category.setStatus(CategoryStatus.ACTIVE);
        categoryRepository.save(category);

        }
        // helper functions so that we dont have to repeat this on the top.
        private Category getCategoryOrThrow(Long id){
        return categoryRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Category not found with id: " + id
                )
        );

        }

}
