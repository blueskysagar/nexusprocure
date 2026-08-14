package com.nexusprocure.product.dto.response;

import com.nexusprocure.product.enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductResponse {
    private Long id;
    private String productCode;
    private String name;
    private String description;
    private BigDecimal price;
    private ProductStatus status;
    private Long categoryId;
    private String categoryName;
}
