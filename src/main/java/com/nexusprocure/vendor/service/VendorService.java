package com.nexusprocure.vendor.service;

import com.nexusprocure.vendor.dto.request.VendorRequestDTO;
import com.nexusprocure.vendor.dto.request.VendorUpdateRequestDTO;
import com.nexusprocure.vendor.dto.response.VendorResponseDTO;
import com.nexusprocure.vendor.entity.Vendor;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface VendorService {
    VendorResponseDTO createVendor(VendorRequestDTO request);
    VendorResponseDTO getVendorById(Long id);
    Page<VendorResponseDTO> getAllVendors(Pageable pageable);
    Page<VendorResponseDTO> searchVendors(String keyword, Pageable pageable);
    VendorResponseDTO updateVendor(Long id, VendorUpdateRequestDTO request);
    void deleteVendor(Long id);



}
