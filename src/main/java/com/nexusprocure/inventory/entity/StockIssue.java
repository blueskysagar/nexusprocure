package com.nexusprocure.inventory.entity;

import com.nexusprocure.common.base.BaseEntity;
import com.nexusprocure.inventory.enums.StockIssueStatus;
import com.nexusprocure.user.entity.User;
import com.nexusprocure.warehouse.entity.Warehouse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stock_issues")
@Getter
@Setter
@NoArgsConstructor
public class StockIssue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 50)
    private String issueNumber;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requestedBy", nullable = false)
    private User requestedBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approvedBy")
    private User approvedBy;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockIssueStatus status;
    @Column(length = 500)
    private String reason;
    private LocalDateTime issuedDate;
    @OneToMany(mappedBy = "stockIssue",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<StockIssueItem> items = new ArrayList<>();
    public static StockIssue create(
            Warehouse warehouse,
            User requestedBy,
            String reason
    ) {
        StockIssue stockIssue = new StockIssue();
        stockIssue.warehouse = warehouse;
        stockIssue.requestedBy = requestedBy;
        stockIssue.reason = reason;
        stockIssue.status = StockIssueStatus.REQUESTED;
        stockIssue.issueNumber = "SI-" + System.currentTimeMillis();
        return stockIssue;
    }


    public void approve(User approver){
        if(status !=StockIssueStatus.REQUESTED){
            throw new IllegalStateException("Only requested Stock issues can be approved.");
        }
        this.status = StockIssueStatus.APPROVED;
        this.approvedBy = approver;
        this.issuedDate = LocalDateTime.now();
    }

    // If a method changes, protects, and validate its own state then it belongs to this entity
    public void addItem(StockIssueItem item) {
        items.add(item);// add child's to the parent collection
        item.setStockIssue(this); // tell the StockIssueitem which StockIssue it belongs.

    }


}