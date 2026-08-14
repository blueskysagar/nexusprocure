package com.nexusprocure.warehouse.repository;

import com.nexusprocure.warehouse.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long>, JpaSpecificationExecutor<Warehouse> {
    boolean existsByWarehouseCode(String warehouseCode);
    boolean existsByEmail(String email);
    Optional<Warehouse> findByWarehouseCode(String warehouseCode);
    Optional<Warehouse> findTopByOrderByIdDesc();

}
