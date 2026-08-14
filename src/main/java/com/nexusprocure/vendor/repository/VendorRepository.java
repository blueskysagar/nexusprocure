package com.nexusprocure.vendor.repository;

import com.nexusprocure.vendor.entity.Vendor;
import com.nexusprocure.vendor.entity.VendorStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
   Optional<Vendor> findByVendorCode(String vendorCode);
   Optional<Vendor> findByEmail(String email);
   List<Vendor> findByStatus(VendorStatus status);
   List<Vendor> findByVendorNameContainingIgnoreCase(String name);
   boolean existsByVendorCode(String vendorCode);
   boolean existsByEmail(String email);
   // return only active vendors
   Page<Vendor> findByStatus(VendorStatus status, Pageable pageable);
   @Query("""
            SELECT v FROM Vendor v
            WHERE v.status = :status
            AND (LOWER(v.vendorName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(v.vendorCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(v.email) LIKE LOWER(CONCAT('%', :keyword, '%')))
""")
   Page<Vendor> searchVendors(@Param("keyword") String keyword, @Param("status")VendorStatus status ,Pageable pageable);
}
