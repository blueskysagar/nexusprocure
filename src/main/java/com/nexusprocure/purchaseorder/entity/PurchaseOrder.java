package com.nexusprocure.purchaseorder.entity;

import com.nexusprocure.common.base.BaseEntity;
import com.nexusprocure.purchase.entity.PurchaseRequisition;
import com.nexusprocure.vendor.entity.Vendor;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "purchase_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PurchaseOrder extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 30)
    private String purchaseOrderNumber;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_requisition_id", nullable = false)
    private PurchaseRequisition purchaseRequisition;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;
    @Enumerated(EnumType.STRING)
    private PurchaseOrderStatus purchaseOrderStatus;
    @Column(nullable = false)
    private LocalDate orderDate;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;
    @OneToMany(
            mappedBy = "purchaseOrder",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<PurchaseOrderItem> items = new ArrayList<>();
    public void addItem(PurchaseOrderItem item){
        items.add(item); // Update Purchase Order
        item.setPurchaseOrder(this); //Update Purchase Order items
    }
    public void removeItem(PurchaseOrderItem item) {
        items.remove(item);
        item.setPurchaseOrder(null);
    }

}