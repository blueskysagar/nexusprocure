package com.nexusprocure.warehouse.entity;

import com.nexusprocure.common.base.BaseEntity;
import com.nexusprocure.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "warehouses")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Warehouse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "warehouse_code", nullable = false, unique = true, length = 20)
    private String warehouseCode;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, length = 255)
    private String address;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WarehouseStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;
    @Column(nullable = false)
    private Integer capacity;
    @Column(name = "contact_number", nullable = false, length = 20)
    private String contactNumber;
    @Column(nullable = false, unique = true, length = 255)
    private String email;
    @Column(length = 500)
    private String description;
    public void assign(User manager){
        this.manager = manager;
    }
    public void activate(){
        this.status = WarehouseStatus.ACTIVE;
    }
    public void deactivate(){
        this.status = WarehouseStatus.INACTIVE;
    }
    public void updateDetails(String name, String address, Integer capacity, String contactNumber, String email, String description){
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.contactNumber = contactNumber;
        this.email = email;
        this.description = description;
    }
    public void assignWarehouseCode(String warehouseCode){
        this.warehouseCode = warehouseCode;
    }



}
