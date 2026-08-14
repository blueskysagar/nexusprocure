package com.nexusprocure.inventory.entity;

import com.nexusprocure.common.base.BaseEntity;
import com.nexusprocure.inventory.enums.InventoryStatus;
import com.nexusprocure.product.entity.Product;
import com.nexusprocure.warehouse.entity.Warehouse;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inventories",
uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_inventory_product_warehouse",
                columnNames = {
                        "product_id",
                        "warehouse_id"
                }
        )
},
        indexes = {
        @Index(
                name = "idx_inventory_product",
                columnList = "product_id"
        ),
                @Index(
                        name = "idx_inventory_warehouse",
                        columnList = "warehouse_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Inventory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;
    @Column(nullable = false)
    @Min(0)
    private Integer quantity = 0;
    @Column(nullable = false)
    @Min(0)
    private Integer reservedQuantity = 0;
    @Column(nullable = false)
    @Min(0)
    private Integer minimumStock = 0;
    @Column(nullable = false)
    @Min(0)
    private Integer maximumStock = 0;
    private Long version;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryStatus inventoryStatus;
    @OneToMany(
            mappedBy = "inventory",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<InventoryMovement> movements = new ArrayList<>();
    public void addMovement(InventoryMovement movement){
        movements.add(movement); // Inventory under that movements=MovementSTOCK_ISSUE, 10, SI-1001
        // Inventory keeps movements into its collection
        movement.setInventory(this);
        // says which specific inventory this movement belongs. These are only used where there is many to one relationship
    }
    public void receive(Integer quantity){
        if(quantity == null || quantity <=0){
            throw new IllegalArgumentException("Received quantity must be greater than zero.");
        }
        this.quantity += quantity;
    }
    public void issue(Integer quantity){
        if(quantity == null || quantity <=0){
            throw new IllegalArgumentException("Received quantity must be greater than zero.");
        }
        if(this.quantity < quantity){
            throw new IllegalStateException("Insufficient inventory available.");
        }
        this.quantity -= quantity;
    }

}
