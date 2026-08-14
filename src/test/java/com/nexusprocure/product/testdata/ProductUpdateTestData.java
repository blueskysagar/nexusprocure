package com.nexusprocure.product.testdata;

import com.nexusprocure.product.dto.request.ProductRequest;
import com.nexusprocure.product.dto.response.ProductResponse;
import com.nexusprocure.product.dto.update.ProductUpdateRequest;
import com.nexusprocure.product.entity.Category;
import com.nexusprocure.product.entity.Product;
import com.nexusprocure.product.enums.ProductStatus;

import java.math.BigDecimal;

public final class ProductUpdateTestData {
    private ProductUpdateTestData(){}
    private static final String UPDATED_NAME = "Dell Latitude Updated";
    private static final String UPDATED_DESCRIPTION = "Updated Dell Latitude 5440";
    private static final BigDecimal UPDATED_PRICE = BigDecimal.valueOf(1800);
    private static final Long UPDATED_CATEGORY_ID = 1L;

    public static ProductUpdateRequest request() {

        ProductUpdateRequest request = new ProductUpdateRequest();

        request.setName(UPDATED_NAME);
        request.setDescription(UPDATED_DESCRIPTION);
        request.setPrice(UPDATED_PRICE);
        request.setCategoryId(UPDATED_CATEGORY_ID);

        return request;
    }






}
