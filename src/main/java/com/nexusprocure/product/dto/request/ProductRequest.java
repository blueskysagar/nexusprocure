package com.nexusprocure.product.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequest {
    @NotBlank(message = "Product name is required")
    @Size(max = 150, message = "Product name must not exceed more than 100 characters")
    private String name;
    @Size(max = 1000, message = "Description must not exceed more than 1000 characters")
    private String description;
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    @Digits(integer = 17, fraction = 2, message = "Price must have up to 17 digits and 2 decimal places")
    private BigDecimal price;
    @NotNull(message = "Category is required")
    private Long categoryId;

}
