package com.nexusprocure.product.testdata;

import com.nexusprocure.common.Response.PageResponse;
import com.nexusprocure.product.dto.request.ProductFilterRequest;
import com.nexusprocure.product.dto.request.ProductRequest;
import com.nexusprocure.product.dto.response.ProductResponse;
import com.nexusprocure.product.entity.Category;
import com.nexusprocure.product.entity.Product;
import com.nexusprocure.product.enums.ProductStatus;

import java.math.BigDecimal;
import java.util.List;

public final class ProductTestData {
   private ProductTestData(){}
    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final String DEFAULT_PRODUCT_CODE = "PRD-00001";
    private static final String DEFAULT_NAME = "Dell Laptop";
    private static final String DEFAULT_DESCRIPTION = "Dell Latitude 5440";
    private static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(1500);
    private static final ProductStatus DEFAULT_STATUS = ProductStatus.ACTIVE;

    private static final Long DEFAULT_CATEGORY_ID = 1L;
    private static final String DEFAULT_CATEGORY_NAME = "Electronics";

    public static Product entity() {

        Product product = new Product();
        product.setProductCode(DEFAULT_PRODUCT_CODE);
        product.setName(DEFAULT_NAME);
        product.setDescription(DEFAULT_DESCRIPTION);
        product.setPrice(DEFAULT_PRICE);
        product.setCategory(CategoryTestData.entity());
        product.setProductStatus(DEFAULT_STATUS);

        return product;
    }
    public static Product entityWithId(){

        Product product = entity();

        product.setId(DEFAULT_PRODUCT_ID);

        return product;
    }

    public static ProductRequest request() {

        ProductRequest request = new ProductRequest();

        request.setName(DEFAULT_NAME);
        request.setDescription(DEFAULT_DESCRIPTION);
        request.setPrice(DEFAULT_PRICE);
        request.setCategoryId(DEFAULT_CATEGORY_ID);

        return request;
    }

    public static ProductResponse response() {

        ProductResponse response = new ProductResponse();

        response.setId(DEFAULT_PRODUCT_ID);
        response.setProductCode(DEFAULT_PRODUCT_CODE);
        response.setName(DEFAULT_NAME);
        response.setDescription(DEFAULT_DESCRIPTION);
        response.setPrice(DEFAULT_PRICE);
        response.setCategoryId(DEFAULT_CATEGORY_ID);
        response.setCategoryName(DEFAULT_CATEGORY_NAME);
        response.setStatus(DEFAULT_STATUS);

        return response;
    }

    public static ProductFilterRequest emptyFilter() {
        return new ProductFilterRequest();
    }

    public static ProductFilterRequest categoryFilter() {

        ProductFilterRequest request = new ProductFilterRequest();
        request.setCategoryId(DEFAULT_CATEGORY_ID);

        return request;
    }

    public static ProductFilterRequest statusFilter() {

        ProductFilterRequest request = new ProductFilterRequest();
        request.setProductStatus(DEFAULT_STATUS);

        return request;
    }

    public static ProductFilterRequest keywordFilter() {

        ProductFilterRequest request = new ProductFilterRequest();
        request.setKeyword(DEFAULT_NAME);

        return request;
    }

    public static ProductFilterRequest minimumPriceFilter() {

        ProductFilterRequest request = new ProductFilterRequest();
        request.setMinPrice(BigDecimal.valueOf(1000));

        return request;
    }

    public static ProductFilterRequest maximumPriceFilter() {

        ProductFilterRequest request = new ProductFilterRequest();
        request.setMaxPrice(BigDecimal.valueOf(2000));

        return request;
    }

    public static ProductFilterRequest completeFilter() {

        ProductFilterRequest request = new ProductFilterRequest();

        request.setCategoryId(DEFAULT_CATEGORY_ID);
        request.setProductStatus(DEFAULT_STATUS);
        request.setKeyword(DEFAULT_NAME);
        request.setMinPrice(BigDecimal.valueOf(1000));
        request.setMaxPrice(BigDecimal.valueOf(2000));

        return request;
    }

    public static PageResponse<ProductResponse> pageResponse() {

        PageResponse<ProductResponse> response = new PageResponse<>();

        response.setContent(List.of(response(), response()));
        response.setPage(0);
        response.setSize(10);
        response.setTotalElements(2);
        response.setTotalPages(1);

        return response;
    }


}
