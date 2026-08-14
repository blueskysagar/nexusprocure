package com.nexusprocure.product.dto.request;

import com.nexusprocure.product.enums.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductFilterRequest {
    @Schema(description = "Filter products by status", example = "ACTIVE")
    private ProductStatus productStatus;
    @Schema(description = "Filter by categoryID", example = "1")
    private Long categoryId;
    @Schema(description = "Search by product name or description", example = "dell")
    private String keyword;
    @Schema(description = "minimumProductPrice", example = "500")
    private BigDecimal minPrice;
    @Schema(description = "maximumProductPrice", example = "3000")
    private BigDecimal maxPrice;
}
