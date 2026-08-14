package com.nexusprocure.inventory.repository;

import com.nexusprocure.inventory.entity.ProcessedStockIssue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedStockIssueRepository extends JpaRepository<ProcessedStockIssue, Long> {
boolean existsByStockIssueId(Long stockIssueId);
}
