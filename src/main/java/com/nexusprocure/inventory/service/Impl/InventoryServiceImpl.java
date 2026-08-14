package com.nexusprocure.inventory.service.Impl;


import com.nexusprocure.common.Response.PageResponse;
import com.nexusprocure.common.Response.PageResponseMapper;
import com.nexusprocure.common.cache.CacheNames;
import com.nexusprocure.exception.DuplicateResourceException;
import com.nexusprocure.exception.InvalidInventoryConfigurationException;
import com.nexusprocure.exception.ResourceNotFoundException;
import com.nexusprocure.inventory.dto.request.InventoryFilterRequest;
import com.nexusprocure.inventory.dto.request.InventoryRequest;
import com.nexusprocure.inventory.dto.response.InventoryResponse;
import com.nexusprocure.inventory.dto.update.InventoryUpdateRequest;
import com.nexusprocure.inventory.entity.Inventory;
import com.nexusprocure.inventory.enums.InventoryStatus;
import com.nexusprocure.inventory.mapper.InventoryMapper;
import com.nexusprocure.inventory.repository.InventoryRepository;
import com.nexusprocure.inventory.service.InventoryService;
import com.nexusprocure.inventory.specification.InventorySpecificationBuilder;
import com.nexusprocure.product.entity.Product;
import com.nexusprocure.product.repository.ProductRepository;
import com.nexusprocure.warehouse.entity.Warehouse;
import com.nexusprocure.warehouse.repository.WarehouseRepository;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    @Transactional
    public InventoryResponse createInventory(InventoryRequest request) {
        log.info(
                "Creating inventory. productId={}, warehouseId={}", request.getProductId(), request.getWarehouseId()
        );
        Product product = findProduct(request.getProductId());
        Warehouse warehouse = findWarehouse(request.getWarehouseId());
        validateDuplicateInventory(product.getId(), warehouse.getId());
        validateStockLevels(request.getMinimumStock(), request.getMaximumStock());
        Inventory inventory = inventoryMapper.toEntity(request);
        inventory.setProduct(product);
        inventory.setWarehouse(warehouse);
        inventory.setReservedQuantity(0);
        inventory.setInventoryStatus(InventoryStatus.ACTIVE);
        Inventory savedInventory = inventoryRepository.save(inventory);
        log.info("Inventory created successfully. inventoryId={}", savedInventory.getId());
        return inventoryMapper.toResponse(savedInventory);

    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Product not found. productId{}", productId);
                    return new ResourceNotFoundException("Product not found");
                });
    }

    private Warehouse findWarehouse(Long warehouseId) {
        return warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> {
                    log.warn("Warehouse not found: warehouseId={}", warehouseId);
                    return new ResourceNotFoundException("Warehouse not found");
                });

    }

    private void validateDuplicateInventory(Long productId, Long warehouseId) {
        if (inventoryRepository.findByProduct_IdAndWarehouse_Id(productId, warehouseId).isPresent()) {
            log.warn("Inventory already exists. productId={}, warehouseId={}", productId, warehouseId);

        throw new DuplicateResourceException("Inventory already exists for this product and warehouse");
    }
}
   private void validateStockLevels(Integer minimumStock, Integer maximumStock){
       if(minimumStock == null || maximumStock == null){
           return;
       }
        if(minimumStock > maximumStock){
            log.warn("Invalid Stock Configuration. minimumStock={}, maximumStock={}", minimumStock, maximumStock);
            throw new InvalidInventoryConfigurationException("Minimum stock cannot be greater than maximum stock.");
        }
   }

   @Override
   @Cacheable(
           value = CacheNames.INVENTORY,
           key = "nexusProcureKeyGenerator"
   )
    @Transactional(readOnly = true)
    public InventoryResponse getInventoryById(Long id){
        log.info("Fetching inventory. inventoryId={}", id);
        Inventory inventory = findInventory(id);
        log.info("Inventory fetched successfully. inventoryId={}", id);
        return inventoryMapper.toResponse(inventory);
   }
   private Inventory findInventory(Long inventoryId){
        return inventoryRepository.findById(inventoryId).orElseThrow(() -> {
            log.warn("Inventory not found. inventoryId={}", inventoryId);
            return new ResourceNotFoundException("Inventory not found.");
        });
   }

@Override
@Transactional(readOnly = true)
    public PageResponse<InventoryResponse> getAllInventories(InventoryFilterRequest request, Pageable pageable){
        log.info("Fetching inventories. page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
    Specification<Inventory> specification = InventorySpecificationBuilder.build(request);
    Page<Inventory> inventories = inventoryRepository.findAll(specification, pageable);
    log.info("Inventories fetched successfully. totalElements={}, totalPages={}", inventories.getTotalElements(), inventories.getTotalPages());
    Page<InventoryResponse> responsePage = inventories.map(inventoryMapper::toResponse);
    return PageResponseMapper.toResponse(responsePage);
}
@Override
    @Transactional
    public InventoryResponse updateInventory(Long id, InventoryUpdateRequest request){
        log.info("Updating inventory. inventoryId={}",id);
        Inventory inventory = findInventory(id);
        validateStockLevels(request.getMinimumStock(), request.getMaximumStock());
        inventory.setMinimumStock(request.getMinimumStock());
        inventory.setMaximumStock(request.getMaximumStock());
        log.info("Inventory updated successfully. inventoryId={}", inventory.getId());
        return inventoryMapper.toResponse(inventory);
}
@Override
    @Transactional
    public void activateInventory(Long id){
        log.info("Activating inventory. inventoryId={}", id);
        Inventory inventory = findInventory(id);
        inventory.setInventoryStatus(InventoryStatus.ACTIVE);
        log.info("Inventory activated successfully. inventoryId={}", id);
}
@Override
    @Transactional
    public void deactivateInventory(Long id){
        log.info("Deactivating inventory. inventoryId={}", id);
        Inventory inventory = findInventory(id);
        inventory.setInventoryStatus(InventoryStatus.INACTIVE);
        log.info("Inventory deactivated successfully. inventoryId={}", id);
}








}
