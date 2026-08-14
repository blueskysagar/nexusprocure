package com.nexusprocure.inventory.service.Impl;

import com.nexusprocure.common.event.stockissue.StockIssueApprovedApplicationEvent;
import com.nexusprocure.common.event.stockissue.StockIssueApprovedEvent;
import com.nexusprocure.common.event.stockissue.StockIssueItemEvent;
import com.nexusprocure.exception.*;
import com.nexusprocure.inventory.dto.request.StockIssueRequest;
import com.nexusprocure.inventory.dto.response.StockIssueResponse;
import com.nexusprocure.inventory.entity.Inventory;
import com.nexusprocure.inventory.entity.StockIssue;
import com.nexusprocure.inventory.entity.StockIssueItem;
import com.nexusprocure.inventory.mapper.StockIssueMapper;
import com.nexusprocure.inventory.repository.InventoryRepository;
import com.nexusprocure.inventory.repository.StockIssueRepository;
import com.nexusprocure.inventory.service.StockIssueService;
import com.nexusprocure.product.entity.Product;
import com.nexusprocure.product.repository.ProductRepository;
import com.nexusprocure.user.entity.User;
import com.nexusprocure.user.repository.UserRepository;
import com.nexusprocure.warehouse.entity.Warehouse;
import com.nexusprocure.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nexusprocure.inventory.entity.InventoryMovement;
import com.nexusprocure.inventory.enums.InventoryMovementType;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StockIssueServiceImpl implements StockIssueService {
    private final StockIssueRepository stockIssueRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final StockIssueMapper stockIssueMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    // send a message inside our springboot application to whoever is listening
    // in order to reduce tight coupling and maintenance later becomes easier.
    @Override
    public StockIssueResponse createStockIssue(StockIssueRequest request) {
        Warehouse warehouse = warehouseRepository.findById(request.warehouseId()).orElseThrow(() -> new WarehouseNotFoundException(request.warehouseId()));
        User requestedBy = userRepository.findById(request.requestedById()).orElseThrow(() -> new UserNotFoundException(
                request.requestedById()
        ));
        StockIssue stockIssue = StockIssue.create(
                warehouse,
                requestedBy,
                request.reason()
        );
        request.items()
                .forEach(itemRequest -> {
                    Product product = productRepository.findById(itemRequest.productId()).orElseThrow(() -> new ProductNotFoundException(
                            itemRequest.productId()
                    ));
                    StockIssueItem item = new StockIssueItem();
                    item.setProduct(product);
                    item.setQuantity(itemRequest.quantity());
                    stockIssue.addItem(item);

                });
        StockIssue savedStockIssue = stockIssueRepository.save(stockIssue);
        return stockIssueMapper.toResponse(savedStockIssue);
    }
    @Override
    @Transactional(readOnly = true)
    public StockIssueResponse getStockIssueById(Long id){
        StockIssue stockIssue = stockIssueRepository.findById(id).orElseThrow(()  -> new StockIssueNotFoundException(id));
        return stockIssueMapper.toResponse(stockIssue);
    }
    @Override
    @Transactional
    public StockIssueResponse approveStockIssue(Long stockIssueId, Long approverId){
        StockIssue stockIssue = stockIssueRepository.findById(stockIssueId).orElseThrow(() -> new StockIssueNotFoundException(stockIssueId));
        User approver = userRepository.findById(approverId).orElseThrow(() -> new UserNotFoundException(approverId));
        stockIssue.approve(approver);
        List<StockIssueItemEvent> items = stockIssue.getItems()
                        .stream()
                                .map(item -> new StockIssueItemEvent(item.getProduct().getId(), item.getQuantity()))
                                        .toList();
        //Take every item from this Stock Issue, extract its product ID and quantity, create a lightweight StockIssueItemEvent for it, and collect all those event items into a list."
        StockIssueApprovedEvent event = new StockIssueApprovedEvent(
                stockIssue.getId(),
                stockIssue.getIssueNumber(),
                stockIssue.getWarehouse().getId(),
                stockIssue.getApprovedBy().getId(),
                stockIssue.getIssuedDate(),
                items
        ); // / 5. Create the business event that Inventory will eventually consume.
        applicationEventPublisher.publishEvent(new StockIssueApprovedApplicationEvent(event));
       // This publishes an event inside your Spring application.

        return stockIssueMapper.toResponse(stockIssue);
    }

    }





