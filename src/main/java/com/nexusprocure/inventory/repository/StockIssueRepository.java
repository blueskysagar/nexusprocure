package com.nexusprocure.inventory.repository;

import com.nexusprocure.inventory.entity.StockIssue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockIssueRepository extends JpaRepository<StockIssue, Long> {
}
