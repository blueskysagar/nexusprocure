package com.nexusprocure.inventory.controller;

import com.nexusprocure.authentication.security.CustomUserPrincipal;
import com.nexusprocure.inventory.dto.request.StockIssueRequest;
import com.nexusprocure.inventory.dto.response.StockIssueResponse;
import com.nexusprocure.inventory.service.StockIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock-issues")
@RequiredArgsConstructor
public class StockIssueController {
    private final StockIssueService stockIssueService;
    @PostMapping
    @PreAuthorize("hasAnyRole('WAREHOUSE_STAFF','WAREHOUSE_MANAGER','ADMIN')")
    public ResponseEntity<StockIssueResponse> createStockIssue(@RequestBody StockIssueRequest request){
        StockIssueResponse response = stockIssueService.createStockIssue(request);
        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(response);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('WAREHOUSE_STAFF','WAREHOUSE_MANAGER','ADMIN')")
    public ResponseEntity<StockIssueResponse> getStockIssueById(@PathVariable Long id){
        StockIssueResponse response = stockIssueService.getStockIssueById(id);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('WAREHOUSE_MANAGER','ADMIN')")
    public ResponseEntity<StockIssueResponse> approveStockIssue(@PathVariable Long id, Authentication authentication){
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        Long approverId = principal.getUser().getId();

        return ResponseEntity.ok(stockIssueService.approveStockIssue(id, approverId));
    }






}
