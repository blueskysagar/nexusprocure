package com.nexusprocure.inventory.entity;

import com.nexusprocure.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// We are doing this to check if this event has been proceessed before with that Id otherwise inventory would be reduced twice and that is bug.
@Entity
@Table(name = "processed_stock_issues",
uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_processed_stock_issue",
                columnNames = "stock_issue_id"
        )
})
@Getter
@Setter
@NoArgsConstructor
public class ProcessedStockIssue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name ="stock_issue_id", nullable = false, unique = true)
    private Long stockIssueId;
    public ProcessedStockIssue(Long stockIssueId){
        this.stockIssueId = stockIssueId;
    }



}
