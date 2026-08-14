package com.nexusprocure.integration;

import com.nexusprocure.inventory.repository.InventoryRepository;
import com.nexusprocure.product.repository.ProductRepository;
import com.nexusprocure.warehouse.repository.WarehouseRepository;
import org.springframework.stereotype.Component;

@Component
public class DataBaseCleanUp {
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    public DataBaseCleanUp(InventoryRepository inventoryRepository, ProductRepository productRepository, WarehouseRepository warehouseRepository){
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
    }
    public void clean(){
        inventoryRepository.deleteAll();
        warehouseRepository.deleteAll();
        productRepository.deleteAll();
    }

}
