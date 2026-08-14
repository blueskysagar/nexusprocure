package com.nexusprocure.product.dto.update;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

public class ProductUpdateRequest {
    @NotBlank(message = "Product name is required")
    @Size(max = 150, message = "Product name should not exceed 150 characters.")
    private String name;
    @Size(max = 1000, message = "Description should not exceed 1000 characters.")
    private String description;
    @DecimalMin(value = "0.01", message = "price must be greater than zero.")
    @Digits(integer = 17, fraction = 2, message = "price must have up to 17 digits and 2 decimal places" )
    private BigDecimal price;
    private Long categoryId;
}
