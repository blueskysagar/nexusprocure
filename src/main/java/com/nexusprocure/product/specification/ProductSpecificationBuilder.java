package com.nexusprocure.product.specification;

import com.nexusprocure.product.dto.request.ProductFilterRequest;
import com.nexusprocure.product.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecificationBuilder {
    public static Specification<Product> build(ProductFilterRequest request){
        Specification<Product> specification = ProductFetchSpecification.fetchCategory();
        if(request.getProductStatus() !=null){
            specification = specification.and(ProductSpecification.hasProductStatus(request.getProductStatus()));

        }
        if(request.getCategoryId() !=null){
            specification = specification.and(ProductSpecification.hasCategory(request.getCategoryId()));
        }
        if(request.getKeyword() !=null){
            specification = specification.and(ProductSpecification.hasKeyword(request.getKeyword()));

        }
        if(request.getMinPrice() !=null || request.getMaxPrice() !=null){
            specification = specification.and(ProductSpecification.hasPriceRange(request.getMinPrice(), request.getMaxPrice()));
        }
        return specification;
    }
}
