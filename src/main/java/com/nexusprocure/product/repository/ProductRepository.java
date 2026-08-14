package com.nexusprocure.product.repository;

import com.nexusprocure.product.entity.Product;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByProductCode(String productCode);
    boolean existsByProductCode(String productCode);
    Optional<Product> findTopByOrderByIdDesc();
    @EntityGraph(attributePaths = "category")
    Optional<Product> findById(Long id);




}
