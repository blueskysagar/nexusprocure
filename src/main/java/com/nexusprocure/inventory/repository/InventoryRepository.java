package com.nexusprocure.inventory.repository;

import com.nexusprocure.inventory.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.lang.Nullable;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long>, JpaSpecificationExecutor<Inventory> {
    @EntityGraph(attributePaths = {"product","warehouse"})
    Page<Inventory> findAll(Specification<Inventory> specification, Pageable pageable);
    Optional<Inventory> findByProduct_IdAndWarehouse_Id(Long productId, Long warehouseId);
    boolean existsByProductId(Long productId);
    boolean existsByWarehouseId(Long warehouseId);
}
