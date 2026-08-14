package com.nexusprocure.purchase.entity;

import com.nexusprocure.common.base.BaseEntity;
import com.nexusprocure.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "purchase_requisitions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseRequisition extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 30)
    private String requisitionNumber;
    @Column(nullable = false, length = 150)
    private String title;
    @Column(length = 1000)
    private String description;
    @Column(nullable = false, length = 100)
    private String department;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequisitionPriority requisitionPriority;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequisitionStatus status;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;
    @Column(nullable = false)
    private LocalDate requiredDate;
    @OneToMany(
            mappedBy = "purchaseRequisition",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<PurchaseRequisitionItem> items = new ArrayList<>();


//    @PrePersist
//    public void onCreate(){
//        LocalDateTime now = LocalDateTime.now();
//        this.createdAt = now;                         These are life cycle annotations
//        this.updatedAt = now;
//    }
//    @PreUpdate
//    public void onUpdate(){
//        this.updatedAt = LocalDateTime.now();
//    }
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requested_by", nullable = false)
   private User requestBy;


}
