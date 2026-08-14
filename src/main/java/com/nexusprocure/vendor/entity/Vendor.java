package com.nexusprocure.vendor.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vendors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String vendorCode;
    @Column(nullable = false)
    private String vendorName;
    @Column(nullable = false, unique = true)
    private String email;
    private String phone;
    private String address;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VendorStatus status;
}
