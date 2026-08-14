package com.nexusprocure.inventory.service.Impl;

import com.nexusprocure.common.event.stockissue.StockIssueApprovedEvent;
import com.nexusprocure.common.event.stockissue.StockIssueItemEvent;
import com.nexusprocure.exception.InventoryNotFoundException;
import com.nexusprocure.inventory.entity.Inventory;
import com.nexusprocure.inventory.entity.InventoryMovement;
import com.nexusprocure.inventory.entity.ProcessedStockIssue;
import com.nexusprocure.inventory.enums.InventoryMovementType;
import com.nexusprocure.inventory.repository.InventoryRepository;
import com.nexusprocure.inventory.repository.ProcessedStockIssueRepository;
import com.nexusprocure.inventory.service.InventoryStockIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryStockIssueServiceImpl implements InventoryStockIssueService {
    private final InventoryRepository inventoryRepository;
    private final ProcessedStockIssueRepository processedStockIssueRepository;
    @Override
    @Transactional
    public void processStockIssue(StockIssueApprovedEvent event){
        if(processedStockIssueRepository.existsByStockIssueId(event.stockIssueId())){return; }

            // An approved Stock Issue can contain multiple products.
        // Process each product against its corresponding inventory record.

            for(StockIssueItemEvent item : event.items()){
            Inventory inventory =
                    inventoryRepository
                            .findByProduct_IdAndWarehouse_Id(
                                    item.productId(),
                                    event.warehouseId()
                            )
                            .orElseThrow(() ->
                                    new InventoryNotFoundException(
                                            item.productId()
                                    ));
            inventory.issue(item.quantity());// here issue function is called which is in entity it will do the reduction in entity

            InventoryMovement movement = InventoryMovement.create(
                    InventoryMovementType.STOCK_ISSUE,
                    item.quantity(),
                    event.issueNumber()
                    // It will give me the reason why the quantity changed only but not who
            );
            inventory.addMovement(movement);
            //attach the stock movement to the inventory so both sides of the relationship stay synchronized.

                // Mark the Stock Issue as processed only after
                // all inventory operations have completed successfully.
                processedStockIssueRepository.save(
                        new ProcessedStockIssue(event.stockIssueId()));
        }
    }

}
