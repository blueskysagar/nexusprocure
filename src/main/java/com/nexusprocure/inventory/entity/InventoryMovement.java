package com.nexusprocure.inventory.entity;

import com.nexusprocure.common.base.BaseEntity;
import com.nexusprocure.inventory.enums.InventoryMovementType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_movements")
@Getter
@Setter
@NoArgsConstructor
public class InventoryMovement extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryMovementType movementType;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, length = 50)
    private String referenceNumber;

    @Column(nullable = false)
    private LocalDateTime movementDate;
    public static InventoryMovement create(
            InventoryMovementType movementType,
            Integer quantity,
            String referenceNumber
    ){
        InventoryMovement movement = new InventoryMovement();
        movement.setMovementType(movementType);
        movement.setQuantity(quantity);
        movement.setReferenceNumber(referenceNumber);
        movement.setMovementDate(LocalDateTime.now());
        return movement;
    }
}
