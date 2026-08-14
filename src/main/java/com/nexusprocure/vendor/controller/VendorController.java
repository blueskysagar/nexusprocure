package com.nexusprocure.vendor.controller;

import com.nexusprocure.vendor.dto.request.VendorRequestDTO;
import com.nexusprocure.vendor.dto.request.VendorUpdateRequestDTO;
import com.nexusprocure.vendor.dto.response.VendorResponseDTO;
import com.nexusprocure.vendor.service.VendorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;

@RestController
@RequestMapping("/vendors")
@RequiredArgsConstructor
public class VendorController {
    private final VendorService vendorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorResponseDTO> createVendor(@Valid @RequestBody VendorRequestDTO request){
     VendorResponseDTO response = vendorService.createVendor(request);
     return ResponseEntity
             .status(HttpStatus.CREATED)
             .body(response);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<VendorResponseDTO> getVendorById(@PathVariable Long id){
        VendorResponseDTO response = vendorService.getVendorById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<VendorResponseDTO>> getAllVendors(Pageable pageable){
        Page<VendorResponseDTO> response = vendorService.getAllVendors(pageable);
        return ResponseEntity.ok(response);

    }
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN' , 'USER')")
    public ResponseEntity<Page<VendorResponseDTO>> searchVendors(@RequestParam String keyword, org.springframework.data.domain.Pageable pageable){
        Page<VendorResponseDTO> response = vendorService.searchVendors(keyword, pageable);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<VendorResponseDTO> updateVendor(@PathVariable Long id, @Valid @RequestBody VendorUpdateRequestDTO request){
        VendorResponseDTO response = vendorService.updateVendor(id, request);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendor(@PathVariable Long id){
        vendorService.deleteVendor(id);
        return ResponseEntity.noContent().build();
    }
}
