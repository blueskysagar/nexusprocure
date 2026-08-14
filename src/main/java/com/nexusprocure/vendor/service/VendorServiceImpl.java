package com.nexusprocure.vendor.service;

import com.nexusprocure.exception.VendorAlreadyExistsException;
import com.nexusprocure.exception.VendorNotFoundException;
import com.nexusprocure.vendor.dto.request.VendorRequestDTO;
import com.nexusprocure.vendor.dto.request.VendorUpdateRequestDTO;
import com.nexusprocure.vendor.dto.response.VendorResponseDTO;
import com.nexusprocure.vendor.entity.Vendor;
import com.nexusprocure.vendor.mapper.VendorMapper;
import com.nexusprocure.vendor.repository.VendorRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.nexusprocure.vendor.entity.VendorStatus;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Service
public class VendorServiceImpl implements VendorService{
    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;
    public VendorServiceImpl(VendorRepository vendorRepository, VendorMapper vendorMapper){
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
    }
    @Override
    public VendorResponseDTO createVendor(VendorRequestDTO request){
        if(vendorRepository.existsByVendorCode(request.getVendorCode())) {
            throw new VendorAlreadyExistsException(request.getVendorCode());

        }
        if(vendorRepository.existsByEmail(request.getEmail())){
            throw new VendorAlreadyExistsException(request.getEmail());

        }
        Vendor vendor = vendorMapper.toEntity(request);
        vendor.setStatus(VendorStatus.ACTIVE);
        Vendor savedVendor = vendorRepository.save(vendor);
        return vendorMapper.toResponse(savedVendor);

    }
    @Override
    public VendorResponseDTO getVendorById(Long id){
        Vendor vendor = vendorRepository.findById(id).orElseThrow(() -> new VendorNotFoundException(id));
        return vendorMapper.toResponse(vendor);

    }
    @Override
    public Page<VendorResponseDTO> getAllVendors(Pageable pageable){
        Page<Vendor> vendors = vendorRepository.findByStatus(VendorStatus.ACTIVE, pageable);
        return vendors.map(vendorMapper::toResponse);
    }
    @Override
    public Page<VendorResponseDTO> searchVendors(String keyword, Pageable pageable){
        Page<Vendor> vendors = vendorRepository.searchVendors(keyword, VendorStatus.ACTIVE, pageable);
        return vendors.map(vendorMapper::toResponse);

    }
    @Override
    public VendorResponseDTO updateVendor(Long id, VendorUpdateRequestDTO request){
        Vendor vendor = vendorRepository.findById(id).orElseThrow(() -> new VendorNotFoundException(id));
        vendor.setVendorName(request.getVendorName());
        vendor.setEmail(request.getEmail());
        vendor.setPhone(request.getPhoneNumber());
        vendor.setAddress(request.getAddress());
        Vendor updatedVendor = vendorRepository.save(vendor);
        return vendorMapper.toResponse(updatedVendor);
    }
    @Override
    public void deleteVendor(Long id){
        Vendor vendor = vendorRepository.findById(id).orElseThrow(() -> new VendorNotFoundException(id));
        vendor.setStatus(VendorStatus.INACTIVE);
        vendorRepository.save(vendor);
    }

}
