package com.nexusprocure.product.service;

import com.nexusprocure.common.Response.PageResponse;
import com.nexusprocure.product.dto.request.ProductFilterRequest;
import com.nexusprocure.product.dto.request.ProductRequest;
import com.nexusprocure.product.dto.response.ProductResponse;
import com.nexusprocure.product.dto.update.ProductUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);

    ProductResponse getProductById(Long id);

    PageResponse<ProductResponse> getAllProducts(ProductFilterRequest request, Pageable pageable);

    ProductResponse updateProduct(Long id, ProductUpdateRequest request);

    void activateProduct(Long id);

    void deactivateProduct(Long id);

}