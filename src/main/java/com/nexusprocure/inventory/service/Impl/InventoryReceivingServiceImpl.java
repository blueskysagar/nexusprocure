package com.nexusprocure.inventory.service.Impl;

import com.nexusprocure.common.event.purchaseorder.PurchaseOrderApprovedEvent;
import com.nexusprocure.common.event.purchaseorder.PurchaseOrderItemEvent;
import com.nexusprocure.exception.ProductNotFoundException;
import com.nexusprocure.exception.WarehouseNotFoundException;
import com.nexusprocure.inventory.entity.Inventory;
import com.nexusprocure.inventory.entity.InventoryMovement;
import com.nexusprocure.inventory.entity.ProcessedPurchaseOrder;
import com.nexusprocure.inventory.enums.InventoryMovementType;
import com.nexusprocure.inventory.enums.InventoryStatus;
import com.nexusprocure.inventory.repository.InventoryMovementRepository;
import com.nexusprocure.inventory.repository.InventoryRepository;
import com.nexusprocure.inventory.repository.ProcessedPurchaseOrderRepository;
import com.nexusprocure.inventory.service.InventoryReceivingService;
import com.nexusprocure.product.entity.Product;
import com.nexusprocure.product.repository.ProductRepository;
import com.nexusprocure.warehouse.entity.Warehouse;
import com.nexusprocure.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryReceivingServiceImpl implements InventoryReceivingService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProcessedPurchaseOrderRepository processedPurchaseOrderRepository;
    @Override
    public void receive(PurchaseOrderApprovedEvent event){
        if(processedPurchaseOrderRepository.existsByPurchaseOrderId(event.purchaseOrderId())){
            return;
        }
        event.items().forEach(item  -> receiveItem(item, event));
        ProcessedPurchaseOrder processedPurchaseOrder = new ProcessedPurchaseOrder();
        processedPurchaseOrder.setPurchaseOrderId(event.purchaseOrderId());
        processedPurchaseOrder.setProcessedAt(LocalDateTime.now());
        processedPurchaseOrderRepository.save(processedPurchaseOrder);

    }
    public void receiveItem(PurchaseOrderItemEvent item, PurchaseOrderApprovedEvent event){
      Inventory inventory = findOrCreateInventory(item);
      inventory.receive(item.quantity());
      inventoryRepository.save(inventory);
      createInventoryMovement(event, inventory, item);

    }
    private void createInventoryMovement(PurchaseOrderApprovedEvent event, Inventory inventory, PurchaseOrderItemEvent item){
        InventoryMovement movement = new InventoryMovement();
        movement.setInventory(inventory);
        movement.setMovementType(InventoryMovementType.PURCHASE_RECEIPT);
        movement.setQuantity(item.quantity());
        movement.setReferenceNumber(event.purchaseOrderNumber());
        movement.setMovementDate(LocalDateTime.now());
        inventoryMovementRepository.save(movement);


    }
    private Inventory findOrCreateInventory(PurchaseOrderItemEvent item){
        return inventoryRepository
                .findByProduct_IdAndWarehouse_Id(item.productId(), item.warehouseId())
                .orElseGet(() -> createInventory(item));
    }
    private Inventory createInventory(PurchaseOrderItemEvent item){
        Product product = productRepository.findById(item.productId())
                .orElseThrow(() -> new ProductNotFoundException(item.productId()));

        Warehouse warehouse = warehouseRepository.findById(item.warehouseId())
                .orElseThrow(() -> new WarehouseNotFoundException(item.warehouseId()));

        Inventory inventory = new Inventory();

        inventory.setProduct(product);
        inventory.setWarehouse(warehouse);
        inventory.setQuantity(0);
        inventory.setReservedQuantity(0);
        inventory.setMinimumStock(0);
        inventory.setMaximumStock(0);
        inventory.setInventoryStatus(InventoryStatus.ACTIVE);

        return inventory;
    }
}
