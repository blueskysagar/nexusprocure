package com.nexusprocure.product.service.impl;

import com.nexusprocure.common.cache.CacheNames;
import com.nexusprocure.common.Response.PageResponse;
import com.nexusprocure.common.Response.PageResponseMapper;
import com.nexusprocure.exception.ResourceNotFoundException;
import com.nexusprocure.product.dto.request.ProductFilterRequest;
import com.nexusprocure.product.dto.request.ProductRequest;
import com.nexusprocure.product.dto.response.ProductResponse;
import com.nexusprocure.product.dto.update.ProductUpdateRequest;
import com.nexusprocure.product.entity.Category;
import com.nexusprocure.product.entity.Product;
import com.nexusprocure.product.enums.ProductStatus;
import com.nexusprocure.product.mapper.ProductMapper;
import com.nexusprocure.product.repository.CategoryRepository;
import com.nexusprocure.product.repository.ProductRepository;
import com.nexusprocure.product.service.ProductService;
import com.nexusprocure.product.specification.ProductSpecificationBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request){
        log.info(
                "Creating product. name='{}', CategoryID={}", request.getName(),request.getCategoryId());

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> {
                    log.warn("Cannot create product.Category not found. categoryId={}", request.getCategoryId());
                    return new ResourceNotFoundException("Category not found");
                });
        Product product = productMapper.toEntity(request);
        product.setCategory(category);
        product.setProductCode(generateProductCode());
        product.setProductStatus(ProductStatus.ACTIVE);
        Product savedProduct = productRepository.save(product);
        log.info("Product Created Successfully. productId={}, productCode={}", savedProduct.getId(), savedProduct.getProductCode());
        return productMapper.toResponse(savedProduct);

    }
    private String generateProductCode(){
        return productRepository.findTopByOrderByIdDesc()
                .map(product -> {
                    Long nextId = product.getId() +1;
                    return String.format("PRD-%05d", nextId);
                })
                .orElse("PRD-00001");
    }
    @Override
    @Cacheable(
            value = CacheNames.PRODUCTS,
            keyGenerator = "nexusProcureKeyGenerator",
            sync = true
    )
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id){
        log.info("Fetching product. productId{}",id);
        Product product = productRepository.findById(id).orElseThrow(()  -> {
            log.warn("Product not found. productId{}", id);

            return new ResourceNotFoundException("Product not found");
        } );
        log.info("Product fetched successfully. productId={}", id);
        return productMapper.toResponse(product);
    }
    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getAllProducts(ProductFilterRequest request, Pageable pageable){
        log.info("Fetching products. page={}, size={}", pageable.getPageNumber(), pageable.getPageSize() );
        Specification<Product> specification = ProductSpecificationBuilder.build(request);
        Page<Product> products = productRepository.findAll(specification, pageable);
        log.info("Product fetched successfully. totalElements={}, totalPages={}",products.getTotalElements(), products.getTotalPages());
        Page<ProductResponse> responsePage = products.map(productMapper::toResponse);
        return PageResponseMapper.toResponse(responsePage);
    }
    @Override
    @CacheEvict(value = CacheNames.PRODUCTS,
                keyGenerator = "nexusProcureKeyGenerator")

    @Transactional
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request){
        log.info("Updating product. productId={}",id);
        Product product = productRepository.findById(id).orElseThrow(() -> {
            log.warn("Cannot update product. Product not found. productId{}", id);
            return new ResourceNotFoundException("Product not found");
        });
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() ->{
            log.warn("Cannot update product. Product not found. productID{}", id);
            return new ResourceNotFoundException("Category not found");
        });
        productMapper.updateEntity(request, product);
        product.setCategory(category);
        log.info(
                "Product updated successfully. productId={}, productCode={}",
                product.getId(),
                product.getProductCode()
        );

        return productMapper.toResponse(product);
    }
    @Override
    @CacheEvict(
            value = CacheNames.PRODUCTS,
            keyGenerator = "nexusProcureKeyGenerator"
    )
    @Transactional
    public void activateProduct(Long id){
        log.info("Activating product. productId={}", id);
        Product product = productRepository.findById(id).orElseThrow(() -> {
                    log.warn("Cannot activate product. Product not found. productId={}", id);
                    return new ResourceNotFoundException("Product not found");
                });
        product.setProductStatus(ProductStatus.ACTIVE);
        log.info("Product activated successfully. productId={}", id);
    }
    @Override
    @CacheEvict(
            value = CacheNames.PRODUCTS,
            keyGenerator = "nexusProcureKeyGenerator"
    )
    @Transactional
    public void deactivateProduct(Long id){
        log.info("Deactivating product. productId={}", id);
        Product product = productRepository.findById(id).orElseThrow(() ->{
            log.warn("Cannot deactivate product. Product not found. productId={}", id);
           return new ResourceNotFoundException("Product not found");
        });
        product.setProductStatus(ProductStatus.INACTIVE);
        log.info("Product deactivated successfully. productId={}", id);
    }

    }




